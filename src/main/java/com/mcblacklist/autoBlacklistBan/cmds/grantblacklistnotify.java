package com.mcblacklist.autoBlacklistBan.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class grantblacklistnotify implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (command.getName().equalsIgnoreCase("blacklistnotify")) {
            if (commandSender.isOp()) {
                if (strings.length != 1) {
                    commandSender.sendMessage("Usage: /blacklistnotify <grant|remove> <player>");
                    return true;
                }
                String playerName = strings[0];
                commandSender.getServer().dispatchCommand(commandSender.getServer().getConsoleSender(), "lp user " + playerName + " permission set blacklist.notify true");
                Player player = commandSender.getServer().getPlayer(playerName);
                if (strings[1].equalsIgnoreCase("remove")) {
                    if (player.hasPermission("blacklist.notify")) {
                        commandSender.getServer().dispatchCommand(commandSender.getServer().getConsoleSender(), "lp user " + playerName + " permission unset blacklist.notify");
                        commandSender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[Blacklist] " + ChatColor.RESET + ChatColor.GREEN + "Role successfully granted!" );
                    } else {
                        commandSender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[Blacklist] " + ChatColor.RESET + ChatColor.RED +   playerName + " does not have the blacklist notify permission.");
                        return true;
                    }
                    commandSender.sendMessage("Removed blacklist notify permission from " + playerName);
                    return true;
                } else if (!strings[1].equalsIgnoreCase("grant")) {
                    commandSender.sendMessage("Usage: /blacklistnotify <grant|remove> <player>");
                    return true;
                }
                commandSender.sendMessage("Granted blacklist notify permission to " + playerName);
                return true;
            } else {
                commandSender.sendMessage("You do not have permission to use this command.");
                return true;
            }
        }
        return false;
    }
}
