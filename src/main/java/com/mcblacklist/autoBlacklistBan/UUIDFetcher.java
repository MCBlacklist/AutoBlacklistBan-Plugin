package com.mcblacklist.autoBlacklistBan;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class UUIDFetcher {
    public static String getUsername(UUID uuid) throws Exception {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + uuid);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);

        if (conn.getResponseCode() != 200) throw new IllegalStateException("Player not found: " + uuid);

        JsonObject obj = JsonParser.parseReader(new InputStreamReader(conn.getInputStream())).getAsJsonObject();

        return obj.get("username").getAsString();
    }
}
