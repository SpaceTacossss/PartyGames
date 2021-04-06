package net.spacetacos.partygames.games;

import net.spacetacos.partygames.PartyGamesGame;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

public class OceanParkour extends PartyGame {
    private final List<Location> checkpoints = getPartyGamesGame().getPlugin().getConfigLocations("oceanparkour.checkpoints");
    private Map<UUID, Integer> playerCheckpoints = new HashMap<>();
    private List<UUID> winners = new ArrayList<>();


    public OceanParkour(PartyGamesGame partyGamesGame) {
        super("Ocean Parkour", partyGamesGame);
    }

    @Override
    public void start() {
        getPartyGamesGame().getScoreboard().setCollisionRule(false);
        for (Player player : getPartyGamesGame().getOnlinePlayers()) {
            setupPlayer(player, checkpoints.get(0));
            playerCheckpoints.put(player.getUniqueId(), 0);
        }
    }

    @Override
    public void end() {
        for (Player player : getPartyGamesGame().getOnlinePlayers()) {
            getPartyGamesGame().getScoreboard().setCollisionRule(true);
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Ocean Parkour Winners:");
            int i = 1;
            for (UUID placement : winners) {
                player.sendMessage("#" + i + " - " + getName(placement));
                i++;
            }

            player.setGameMode(GameMode.CREATIVE);
        }
        getPartyGamesGame().setGame(null);
        getPartyGamesGame().close(); // FIX THIS LATER
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        Block block = event.getTo().getBlock();
        Player player = event.getPlayer();
        if (block.getRelative(BlockFace.DOWN).getType() == Material.GOLD_BLOCK) {
            int nextCheckpoint = playerCheckpoints.get(player.getUniqueId()) + 1;
            if (checkpoints.get(nextCheckpoint).distanceSquared(event.getTo()) <= .25) {

                playerCheckpoints.put(player.getUniqueId(), nextCheckpoint);
                player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                player.sendMessage(ChatColor.GREEN + "You have reached a new checkpoint!");

                if (nextCheckpoint == checkpoints.size() - 1) {

                    winners.add(player.getUniqueId());
                    player.setGameMode(GameMode.SPECTATOR);

                    for (Player recipient : getPartyGamesGame().getOnlinePlayers()) {
                        recipient.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName() + " has finished the parkour!");
                    }

                    if (winners.size() == playerCheckpoints.size()) {
                        end();
                    }
                }
            }
        }
        else if (block.getType() == Material.WATER)  {
            if (player.getGameMode() == GameMode.ADVENTURE) {
                player.teleport(checkpoints.get(playerCheckpoints.get(player.getUniqueId())));
            }
        }
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }
}
