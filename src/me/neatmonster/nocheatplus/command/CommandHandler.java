package me.neatmonster.nocheatplus.command;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.neatmonster.nocheatplus.NoCheatPlus;
import me.neatmonster.nocheatplus.NoCheatPlusPlayer;
import me.neatmonster.nocheatplus.checks.chat.ChatCheck;
import me.neatmonster.nocheatplus.checks.chat.ChatConfig;
import me.neatmonster.nocheatplus.config.Permissions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

/**
 * Handle all NoCheatPlus related commands in a common place
 */
public class CommandHandler {

    private final List<Permission> perms;

    public CommandHandler(final NoCheatPlus plugin) {
        // Make a copy to allow sorting
        perms = new LinkedList<Permission>(plugin.getDescription().getPermissions());

        // Sort NoCheats permission by name and parent-child relation with
        // a custom sorting method
        Collections.sort(perms, new Comparator<Permission>() {

            @Override
            public int compare(final Permission o1, final Permission o2) {

                final String name1 = o1.getName();
                final String name2 = o2.getName();

                if (name1.equals(name2))
                    return 0;

                if (name1.startsWith(name2))
                    return 1;

                if (name2.startsWith(name1))
                    return -1;

                return name1.compareTo(name2);
            }
        });
    }

    /**
     * Handle a command that is directed at NoCheatPlus
     * 
     * @param plugin
     * @param sender
     * @param command
     * @param label
     * @param args
     * @return
     */
    public boolean handleCommand(final NoCheatPlus plugin, final CommandSender sender, final Command command,
            final String label, final String[] args) {

        if (sender instanceof Player) {
            final NoCheatPlusPlayer player = plugin.getPlayer((Player) sender);
            final ChatConfig cc = ChatCheck.getConfig(player);

            // Hide NoCheatPlus's commands if the player doesn't have the required permission
            if (cc.hideCommands && !sender.hasPermission("nocheatplus.admin.commands")) {
                sender.sendMessage("Unknown command. Type \"help\" for help.");
                return true;
            }
        }

        boolean result = false;
        // Not our command, how did it get here?
        if (!command.getName().equalsIgnoreCase("nocheatplus") || args.length == 0)
            result = false;
        else if (args[0].equalsIgnoreCase("permlist") && args.length >= 2)
            // permlist command was used
            result = handlePermlistCommand(plugin, sender, args);
        else if (args[0].equalsIgnoreCase("reload"))
            // reload command was used
            result = handleReloadCommand(plugin, sender);
        else if (args[0].equalsIgnoreCase("playerinfo") && args.length >= 2)
            // playerinfo command was used
            result = handlePlayerInfoCommand(plugin, sender, args);

        return result;
    }

    private boolean handlePermlistCommand(final NoCheatPlus plugin, final CommandSender sender, final String[] args) {

        // Get the player by name
        final Player player = plugin.getServer().getPlayerExact(args[1]);
        if (player == null) {
            sender.sendMessage("Unknown player: " + args[1]);
            return true;
        }

        // Should permissions be filtered by prefix?
        String prefix = "";
        if (args.length == 3)
            prefix = args[2];

        sender.sendMessage("Player " + player.getName() + " has the permission(s):");

        for (final Permission permission : perms)
            if (permission.getName().startsWith(prefix))
                sender.sendMessage(permission.getName() + ": " + player.hasPermission(permission));
        return true;
    }

    private boolean handlePlayerInfoCommand(final NoCheatPlus plugin, final CommandSender sender, final String[] args) {

        final Map<String, Object> map = plugin.getPlayerData(args[1]);
        String filter = "";

        if (args.length > 2)
            filter = args[2];

        sender.sendMessage("PlayerInfo for " + args[1]);
        for (final Entry<String, Object> entry : map.entrySet())
            if (entry.getKey().contains(filter))
                sender.sendMessage(entry.getKey() + ": " + entry.getValue());
        return true;
    }

    private boolean handleReloadCommand(final NoCheatPlus plugin, final CommandSender sender) {

        // Players need a special permission for this
        if (!(sender instanceof Player) || sender.hasPermission(Permissions.ADMIN_RELOAD)) {
            sender.sendMessage("[NoCheatPlus] Reloading configuration");
            plugin.reloadConfiguration();
            sender.sendMessage("[NoCheatPlus] Configuration reloaded");
        } else
            sender.sendMessage("You lack the " + Permissions.ADMIN_RELOAD + " permission to use 'reload'");

        return true;
    }
}
