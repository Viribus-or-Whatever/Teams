package me.goodwilled.teams.commands;

import me.goodwilled.teams.Team;
import me.goodwilled.teams.TeamsPlugin;
import me.goodwilled.teams.gui.TeamsGui;
import me.goodwilled.teams.utils.ColourUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;


public class TeamsCommand implements CommandExecutor {
    private final TeamsPlugin teamsPlugin;

    public TeamsCommand(TeamsPlugin teamsPlugin) {
        this.teamsPlugin = teamsPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        final Player player = (Player) sender;

        if (args.length == 0) {
            player.openInventory(TeamsGui.forPlayer(player));
            return true;
        }

        final String argument = args[0].toLowerCase(Locale.ROOT);

        if (argument.equalsIgnoreCase("leave")) {
            final Team currentTeam = this.teamsPlugin.getTeamManager().getTeam(player.getUniqueId());

            if (currentTeam == Team.CITIZEN) {
                player.sendMessage(ColourUtils.colour(TeamsPlugin.PREFIX + "&cYou are not currently on a team."));
                return true;
            }

            this.teamsPlugin.getTeamManager().setTeam(player.getUniqueId(), Team.CITIZEN, ignored ->
                    player.sendMessage(ColourUtils.colour(TeamsPlugin.PREFIX + "&cYou have left your team."))
            );
            return true;
        }

        if (argument.equalsIgnoreCase("reload") && sender.hasPermission("teams.admin")) {
            long elapsed;
            if (args.length > 1) {
                final String[] flags = new String[args.length - 1];
                System.arraycopy(args, 1, flags, 0, args.length - 1);
                elapsed = this.teamsPlugin.reload(flags);
            } else {
                elapsed = this.teamsPlugin.reload("-config");
            }
            sender.sendMessage(ColourUtils.colour(TeamsPlugin.PREFIX + "&aPlugin reloaded in " + elapsed + "ms."));
        }

        return true;
    }
}
