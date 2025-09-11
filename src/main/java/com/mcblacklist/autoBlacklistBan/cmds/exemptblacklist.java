package com.mcblacklist.autoBlacklistBan.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class exemptblacklist implements CommandExecutor {
    public static List<UUID> exemptedPlayers = new ArrayList<>();
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.isOp()) return true;
        if (command.getName().equalsIgnoreCase("exemptblacklist")) {
            if (strings.length != 1) {
                commandSender.sendMessage("Usage: /exemptblacklist <player>");
                return true;
            }
            if (strings[0].length() > 16) {
                commandSender.sendMessage("Player names cannot be longer than 16 characters.");
                return true;
            }
            if (strings[0].length() < 3) {
                commandSender.sendMessage("Player names cannot be shorter than 3 characters.");
                return true;
            }

            String playerName = strings[0];
            Player player = Bukkit.getPlayerExact(playerName);

            if (player == null) {
                commandSender.sendMessage("Player " + playerName + " not found.");
                return true;
            }

            if (exemptedPlayers.contains(player.getUniqueId())) {
                commandSender.sendMessage("Player " + playerName + " is already exempted from the blacklist.");
                return true;
            }

            exemptedPlayers.add(player.getUniqueId());
            return true;
        }
        return false;
    }
}
