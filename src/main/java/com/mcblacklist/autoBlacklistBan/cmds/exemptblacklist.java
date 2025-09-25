package com.mcblacklist.autoBlacklistBan.cmds;

import com.mcblacklist.autoBlacklistBan.Managers.ExemptManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class exemptblacklist implements CommandExecutor, TabCompleter {
    private final ExemptManager exemptManager;

    public exemptblacklist(ExemptManager exemptManager) {
        this.exemptManager = exemptManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 2) return false;

        if (args[0].equalsIgnoreCase("add")) {
            UUID uuid = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
            exemptManager.addExempt(uuid);
            sender.sendMessage("§aAdded " + args[1] + " to exemptions.");
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            UUID uuid = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
            exemptManager.removeExempt(uuid);
            sender.sendMessage("§cRemoved " + args[1] + " from exemptions.");
            return true;
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return List.of("add", "remove");
        }
        return null;
    }
}

