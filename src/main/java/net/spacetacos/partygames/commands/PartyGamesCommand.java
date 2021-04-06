package net.spacetacos.partygames.commands;

import net.spacetacos.partygames.PartyGamesGame;
import net.spacetacos.partygames.PartyGamesPlugin;
import net.spacetacos.partygames.games.OceanParkour;
import net.spacetacos.partygames.games.PartyGame;
import net.spacetacos.partygames.games.SpiralSplash;
import net.trollyloki.minigames.library.managers.Game;
import net.trollyloki.minigames.library.managers.Party;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class PartyGamesCommand implements CommandExecutor {
    private PartyGamesPlugin plugin;

    public PartyGamesCommand(PartyGamesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("start")) {
                if (sender instanceof Player) {
                    Player player = ((Player) sender);
                    Party party = plugin.getMiniGameManager().getParty(player.getUniqueId());
                    if (party != null && party.isModerator(player.getUniqueId())) {
                        if (party.size() <= 8) {
                            if (args.length > 1) {
                                Function<PartyGamesGame, PartyGame> constructor = null;

                                if (args[1].equalsIgnoreCase("parkour")) {
                                    constructor = OceanParkour::new;
                                }

                                else if (args[1].equalsIgnoreCase("spiral")) {
                                    constructor = SpiralSplash::new;
                                }

                                else {
                                    sender.sendMessage(ChatColor.RED + "Error: Invalid game name");
                                    return false;
                                }

                                PartyGamesGame game = new PartyGamesGame(plugin.getMiniGameManager(), plugin);
                                game.addAll(party);

                                PartyGame partyGame = constructor.apply(game);
                                game.setGame(partyGame);

                                partyGame.start();
                                sender.sendMessage(ChatColor.GREEN + "Success! Game started");
                                return true;
                            }
                            else {
                                sender.sendMessage(ChatColor.RED + "Error: You need to specify the game to start");
                                return false;
                            }
                        }
                        else {
                            sender.sendMessage(ChatColor.RED + "Error: You cannot have more than 8 players in a game");
                            return false;
                        }
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Error: You need to be a party moderator to start a game");
                        return false;
                    }
                }
                else {
                    sender.sendMessage(ChatColor.RED + "Error: Sender not a player");
                    return false;
                }
            }
            else if (args[0].equalsIgnoreCase("end")) {
                if (sender instanceof Player) {
                    Player player = ((Player) sender);
                    Game game = plugin.getMiniGameManager().getGame(player.getUniqueId());
                    if (game instanceof PartyGamesGame) {
                        game.close();
                        sender.sendMessage(ChatColor.GREEN + "Success! Game ended");
                        return true;
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Error: Not in a Party Games game");
                        return false;
                    }
                }
                else {
                    sender.sendMessage(ChatColor.RED + "Error: Sender not a player");
                    return false;
                }
            }
        }
        sender.sendMessage(ChatColor.RED + "Error: Correct command usage is either /" + label + " start or /" + label + " end");
        return false;
    }
}
