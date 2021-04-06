package net.spacetacos.partygames;

import net.spacetacos.partygames.games.PartyGame;
import net.trollyloki.minigames.library.managers.Game;
import net.trollyloki.minigames.library.managers.MiniGameManager;
import net.trollyloki.minigames.library.managers.Party;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class PartyGamesGame extends Game{
    private PartyGamesPlugin plugin;
    private PartyGame game;

    public PartyGamesGame(MiniGameManager manager, PartyGamesPlugin plugin) {

        super(manager);
        this.plugin = plugin;
    }

    public void setGame(PartyGame game) {
        this.game = game;
    }

    public PartyGamesPlugin getPlugin() {
        return plugin;
    }

    @Override
    public int addAll(Party party) {
        for (Player player : party.getOnlinePlayers()) {
            if (add(player.getUniqueId())) {
                getScoreboard().add(player.getUniqueId(), player.getName());
            }
        }
        return 0;
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event) {
        if (game != null) {
            game.onPlayerDamage(event);
        }
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        if (game != null) {
            game.onPlayerMove(event);
        }
    }

}

