package com.mcblacklist.autoBlacklistBan.Managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ExemptManager {
    private final File file;
    private final FileConfiguration config;
    private final Set<UUID> exemptedPlayers = new HashSet<>();

    public ExemptManager(JavaPlugin plugin) {
        file = new File(plugin.getDataFolder(), "exempt.yml");
        if (!file.exists()) {
            plugin.saveResource("exempt.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
        loadExemptions();
    }

    private void loadExemptions() {
        exemptedPlayers.clear();
        for (String uuidStr : config.getStringList("exemptedPlayers")) {
            try {
                exemptedPlayers.add(UUID.fromString(uuidStr));
            } catch (IllegalArgumentException ignored) {}
        }
    }

    public void addExempt(UUID uuid) {
        exemptedPlayers.add(uuid);
        save();
    }

    public void removeExempt(UUID uuid) {
        exemptedPlayers.remove(uuid);
        save();
    }

    public boolean isExempt(UUID uuid) {
        return exemptedPlayers.contains(uuid);
    }

    private void save() {
        config.set("exemptedPlayers", exemptedPlayers.stream().map(UUID::toString).toList());
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<UUID> getExemptedPlayers() {
        return exemptedPlayers;
    }
}
