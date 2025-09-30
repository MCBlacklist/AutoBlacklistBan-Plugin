package com.mcblacklist.autoBlacklistBan.Managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class NotifyManager {
    private final JavaPlugin plugin;
    private final File file;
    private final FileConfiguration config;
    private final Set<UUID> notifyUsers = new HashSet<>();

    public NotifyManager(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;

        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            plugin.getLogger().severe("Failed to create plugin data folder: " + dataFolder.getAbsolutePath());
        }

        this.file = new File(dataFolder, "notify.yml");
        if (!file.exists()) {
            try {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists() && !parent.mkdirs()) {
                    plugin.getLogger().severe("Failed to create directory for notify.yml: " + parent.getAbsolutePath());
                }
                if (!file.createNewFile()) {
                    plugin.getLogger().warning("notify.yml could not be created (it may already exist): " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not create notify.yml at " + file.getAbsolutePath(), e);
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        load();
    }

    private void load() {
        notifyUsers.clear();
        boolean migrated = false;
        List<String> list = config.getStringList("notifyUsers");
        for (String idOrName : list) {
            if (idOrName == null || idOrName.isBlank()) continue;
            String s = idOrName.trim();
            try {
                notifyUsers.add(UUID.fromString(s));
            } catch (IllegalArgumentException ex) {
                try {
                    final Player player = Bukkit.getPlayerExact(s);
                    final UUID uuid;
                    if (player != null) {
                        uuid = player.getUniqueId();
                    } else {
                        final OfflinePlayer offline = Bukkit.getOfflinePlayer(s);
                        uuid = offline.getUniqueId();
                    }
                    notifyUsers.add(uuid);
                    migrated = true;
                } catch (Exception ignored) {
                }
            }
        }
        if (migrated) {
            plugin.getLogger().info("Migrated legacy notify.yml names to UUIDs. Saving updated file...");
            save();
        }
    }

    private void save() {
        config.set("notifyUsers", notifyUsers.stream()
                .map(UUID::toString)
                .sorted()
                .toList());
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save notify.yml at " + file.getAbsolutePath(), e);
        }
    }

    public boolean add(UUID uuid) {
        boolean changed = notifyUsers.add(uuid);
        if (changed) save();
        return changed;
    }

    public boolean remove(UUID uuid) {
        boolean changed = notifyUsers.remove(uuid);
        if (changed) save();
        return changed;
    }

    public boolean contains(UUID uuid) {
        return notifyUsers.contains(uuid);
    }

    @SuppressWarnings("unused")
    public Set<UUID> getAll() {
        return new HashSet<>(notifyUsers);
    }
}
