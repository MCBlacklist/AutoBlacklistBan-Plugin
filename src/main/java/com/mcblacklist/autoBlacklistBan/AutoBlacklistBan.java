package com.mcblacklist.autoBlacklistBan;

import com.mcblacklist.autoBlacklistBan.Listeners.BlacklistListener;
import com.mcblacklist.autoBlacklistBan.Listeners.LoginListener;
import com.mcblacklist.autoBlacklistBan.Managers.BlacklistManager;
import com.mcblacklist.autoBlacklistBan.Managers.ExemptManager;
import com.mcblacklist.autoBlacklistBan.cmds.grantblacklistnotify;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AutoBlacklistBan extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("INITIALIZING BLACKLIST SYSTEM - PLEASE WAIT");

        //managers
        ExemptManager exemptManager = new ExemptManager(this);
        BlacklistManager blacklistManager = new BlacklistManager(this);

        String apiUrl = "http://51.195.102.58/api/recent-blacklists";
        new BlacklistListener(this, apiUrl, blacklistManager).start(20L * 60);

        //cmds
        this.getCommand("blacklistnotify").setExecutor(new grantblacklistnotify());
        this.getCommand("exemptblacklist").setExecutor(new com.mcblacklist.autoBlacklistBan.cmds.exemptblacklist(exemptManager));


        //listeners
        Bukkit.getPluginManager().registerEvents(new LoginListener(exemptManager, blacklistManager), this);

        //tabcompleteres
        this.getCommand("exemptblacklist").setTabCompleter(new com.mcblacklist.autoBlacklistBan.cmds.exemptblacklist(exemptManager));

        Bukkit.getLogger().info("BLACKLIST SYSTEM INITIALIZED - STARTED LISTENING");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("BLACKLIST SYSTEM SHUT DOWN - NO LONGER LISTENING");
    }
}
