package com.mcblacklist.autoBlacklistBan.cmds;

import com.mcblacklist.autoBlacklistBan.Managers.NotifyManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings({"SpellCheckingInspection", "ClassCanBeRecord"})
public class grantblacklistnotify implements CommandExecutor {
    private final NotifyManager notifyManager;
    private static final Component PREFIX = Component.text("[Blacklist] ", NamedTextColor.BLUE).decorate(TextDecoration.BOLD);

    public grantblacklistnotify(NotifyManager notifyManager) {
        this.notifyManager = notifyManager;
    }

    private UUID resolveUuid(String playerName) {
        var player = Bukkit.getPlayerExact(playerName);
        if (player != null) {
            return player.getUniqueId();
        }
        OfflinePlayer offline = Bukkit.getOfflinePlayer(playerName);
        return offline.getUniqueId();
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(PREFIX.append(Component.text("Usage: /blacklistnotify <player> OR /blacklistnotify <grant|remove> <player>", NamedTextColor.YELLOW)));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("blacklistnotify")) {
            return false;
        }

        if (!sender.isOp()) {
            sender.sendMessage(PREFIX.append(Component.text("You do not have permission to use this command.", NamedTextColor.RED)));
            return true;
        }

        if (args.length == 1) {
            String playerName = args[0];
            UUID uuid = resolveUuid(playerName);
            boolean had = notifyManager.contains(uuid);
            if (had) {
                notifyManager.remove(uuid);
                sender.sendMessage(PREFIX.append(Component.text("Removed blacklist notify from ", NamedTextColor.GREEN)
                        .append(Component.text(playerName, NamedTextColor.AQUA))
                        .append(Component.text(" (" + uuid + ")", NamedTextColor.GRAY))));
            } else {
                notifyManager.add(uuid);
                sender.sendMessage(PREFIX.append(Component.text("Granted blacklist notify to ", NamedTextColor.GREEN)
                        .append(Component.text(playerName, NamedTextColor.AQUA))
                        .append(Component.text(" (" + uuid + ")", NamedTextColor.GRAY))));
            }
            return true;
        }

        if (args.length != 2) {
            sendUsage(sender);
            return true;
        }

        String action = args[0];
        String playerName = args[1];
        UUID uuid = resolveUuid(playerName);

        if (action.equalsIgnoreCase("grant")) {
            boolean added = notifyManager.add(uuid);
            if (added) {
                sender.sendMessage(PREFIX.append(Component.text("Granted blacklist notify to ", NamedTextColor.GREEN)
                        .append(Component.text(playerName, NamedTextColor.AQUA))
                        .append(Component.text(" (" + uuid + ")", NamedTextColor.GRAY))));
            } else {
                sender.sendMessage(PREFIX.append(Component.text(playerName, NamedTextColor.YELLOW)
                        .append(Component.text(" already has blacklist notify.", NamedTextColor.GRAY))));
            }
            return true;
        } else if (action.equalsIgnoreCase("remove")) {
            boolean removed = notifyManager.remove(uuid);
            if (removed) {
                sender.sendMessage(PREFIX.append(Component.text("Removed blacklist notify from ", NamedTextColor.GREEN)
                        .append(Component.text(playerName, NamedTextColor.AQUA))
                        .append(Component.text(" (" + uuid + ")", NamedTextColor.GRAY))));
            } else {
                sender.sendMessage(PREFIX.append(Component.text(playerName, NamedTextColor.RED)
                        .append(Component.text(" did not have blacklist notify.", NamedTextColor.GRAY))));
            }
            return true;
        } else {
            sendUsage(sender);
            return true;
        }
    }
}
