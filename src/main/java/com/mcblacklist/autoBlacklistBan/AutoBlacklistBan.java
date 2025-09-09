package com.mcblacklist.autoBlacklistBan;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AutoBlacklistBan extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("INITIALIZING BLACKLIST SYSTEM - PLEASE WAIT");

        String apiUrl = "http://51.195.102.58/api/recent-blacklists";
        new BlacklistListener(this, apiUrl).start(20L * 60);

        Bukkit.getLogger().info("BLACKLIST SYSTEM INITIALIZED - STARTED LISTENING");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("");
    }
}
