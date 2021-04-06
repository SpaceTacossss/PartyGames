package net.spacetacos.partygames.games;

import net.spacetacos.partygames.PartyGamesGame;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public abstract class PartyGame {
    private final PartyGamesGame partyGamesGame;
    private String gameName;

    public PartyGame(String gameName, PartyGamesGame partyGamesGame) {
        this.gameName = gameName;
        this.partyGamesGame = partyGamesGame;
    }
    public String getGameName() {
        return gameName;
    }

    public PartyGamesGame getPartyGamesGame() {
        return partyGamesGame;
    }
    public abstract void start();
    public abstract void end();

    public void onPlayerDamage(EntityDamageEvent event){}
    public void onPlayerMove(PlayerMoveEvent event){}

    public String getName(UUID uuid) {
        return getPartyGamesGame().getScoreboard().getName(uuid);
    }

    public void setupPlayer(Player player, Location location) {
        // Teleport players
        player.teleport(location);
        // Clear inventories
        player.getInventory().clear();
        // Set gamemodes
        player.setGameMode(GameMode.ADVENTURE);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 300, 255));
    }
}

