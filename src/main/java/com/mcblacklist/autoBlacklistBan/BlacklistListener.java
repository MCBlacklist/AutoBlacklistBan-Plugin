package com.mcblacklist.autoBlacklistBan;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class BlacklistListener extends BukkitRunnable {
    private final JavaPlugin plugin;
    private final String apiUrl;
    private final Set<String> knownEntries = new HashSet<>();
    private final Gson gson = new Gson();

    public BlacklistListener(JavaPlugin plugin, String apiUrl) {
        this.plugin = plugin;
        this.apiUrl = apiUrl;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            if (conn.getResponseCode() != 200) {
                Bukkit.getLogger().warning("Blacklist API returned " + conn.getResponseCode() + " - please inform the developer with the code given to you");
                return;
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                JsonArray arr = gson.fromJson(reader, JsonArray.class);
                for (JsonElement el : arr) {
                    JsonObject obj = el.getAsJsonObject();
                    String offenderId = obj.get("offender_discord_id").getAsString();
                    String banDate = obj.get("ban_date").getAsString();
                    String key = offenderId + "_" + banDate;

                    if (!knownEntries.contains(key)) {
                        knownEntries.add(key);

                        String username = obj.get("offender_username").getAsString();
                        String offense = obj.get("offense_type").getAsString();
                        String duration = obj.get("ban_duration").getAsString();

                        Bukkit.getLogger().info(ChatColor.BOLD + "" + ChatColor.BLUE + "[Blacklist] New entry: " + ChatColor.RESET + "" + ChatColor.RED + username + " (" + offense + ", " + duration + ")");
                        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
                        banList.addBan(username, "Banned for: " + offense + " | Duration: " + duration, (Date) null, "© Blacklist");


                    }
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to fetch blacklists: " + e.getMessage());
        }
    }

    public void start(long intervalTicks) {
        this.runTaskTimerAsynchronously(plugin, 0L, intervalTicks);
    }
}
