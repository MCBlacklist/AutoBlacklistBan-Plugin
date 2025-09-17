package com.mcblacklist.autoBlacklistBan.Managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BlacklistManager {
    private final JavaPlugin plugin;
    private File file;
    private FileConfiguration config;
    private final Map<UUID, BlacklistEntry> blacklisted = new HashMap<>();

    public BlacklistManager(JavaPlugin plugin) {
        this.plugin = plugin;
        createFile();
        load();
    }

    private void createFile() {
        file = new File(plugin.getDataFolder(), "blacklisted.yml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void load() {
        blacklisted.clear();
        if (config.isConfigurationSection("blacklisted")) {
            for (String uuidStr : config.getConfigurationSection("blacklisted").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    String reason = config.getString("blacklisted." + uuidStr + ".reason", "Unknown");
                    String duration = config.getString("blacklisted." + uuidStr + ".duration", "Permanent");
                    String expires = config.getString("blacklisted." + uuidStr + ".expires", "Never");
                    blacklisted.put(uuid, new BlacklistEntry(reason, duration, expires));
                } catch (IllegalArgumentException ignored) {}
            }
        }
    }

    public void save() {
        config.set("blacklisted", null);
        for (Map.Entry<UUID, BlacklistEntry> entry : blacklisted.entrySet()) {
            String base = "blacklisted." + entry.getKey().toString();
            BlacklistEntry data = entry.getValue();
            config.set(base + ".reason", data.getReason());
            config.set(base + ".duration", data.getDuration());
            config.set(base + ".expires", data.getExpires());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(UUID uuid, String reason, String duration, String expires) {
        blacklisted.put(uuid, new BlacklistEntry(reason, duration, expires));
        save();
    }

    public void remove(UUID uuid) {
        blacklisted.remove(uuid);
        save();
    }

    public boolean isBlacklisted(UUID uuid) {
        return blacklisted.containsKey(uuid);
    }

    public BlacklistEntry getEntry(UUID uuid) {
        return blacklisted.get(uuid);
    }

    public Map<UUID, BlacklistEntry> getAll() {
        return new HashMap<>(blacklisted);
    }
}
