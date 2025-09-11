package com.mcblacklist.autoBlacklistBan.Listeners;

import com.mcblacklist.autoBlacklistBan.cmds.exemptblacklist;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;


import static com.mcblacklist.autoBlacklistBan.Listeners.BlacklistListener.blacklistedPlayers;

public class LoginListener implements Listener {

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        String name = event.getName().toLowerCase();
        java.util.UUID uuid = event.getUniqueId();

        if (exemptblacklist.exemptedPlayers.contains(uuid)) {
            event.allow();
        }

        if (blacklistedPlayers.contains(uuid)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, BlacklistListener.banMessagefinal);
        }
    }

}
