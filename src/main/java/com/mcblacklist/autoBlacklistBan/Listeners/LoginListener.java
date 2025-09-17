package com.mcblacklist.autoBlacklistBan.Listeners;

import com.mcblacklist.autoBlacklistBan.Managers.BlacklistEntry;
import com.mcblacklist.autoBlacklistBan.Managers.BlacklistManager;
import com.mcblacklist.autoBlacklistBan.Managers.ExemptManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;


public class LoginListener implements Listener {

    private final ExemptManager exemptManager;
    private final BlacklistManager blacklistManager;

    public LoginListener(ExemptManager exemptManager, BlacklistManager blacklistManager) {
        this.exemptManager = exemptManager;
        this.blacklistManager = blacklistManager;
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        if (exemptManager.isExempt(uuid)) {
            event.allow();
            return;
        }

        if (blacklistManager.isBlacklisted(uuid)) {
            BlacklistEntry entry = blacklistManager.getEntry(uuid);
            String banMessage =
                    ChatColor.RED + "==============================\n" +
                            ChatColor.BOLD + "" + ChatColor.RED + "⚠ YOU HAVE BEEN BLACKLISTED! ⚠\n" +
                            ChatColor.RED + "==============================\n\n" +
                            ChatColor.GOLD + "Offense: " + ChatColor.YELLOW + entry.getReason() + "\n" +
                            ChatColor.GOLD + "Duration: " + ChatColor.YELLOW + entry.getDuration() + "\n" +
                            ChatColor.GOLD + "Expires on: " + ChatColor.YELLOW + entry.getExpires() + "\n\n" +
                            ChatColor.GRAY + "Issued by: © Blacklist System\n" +
                            ChatColor.RED + "==============================";

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, banMessage);
        }
    }
}
