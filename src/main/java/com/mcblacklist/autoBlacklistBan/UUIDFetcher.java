package com.mcblacklist.autoBlacklistBan;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class UUIDFetcher {
    public static UUID getUUID(String name) throws Exception {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);

        if (conn.getResponseCode() != 200) throw new IllegalStateException("Player not found: " + name);

        JsonObject obj = JsonParser.parseReader(new InputStreamReader(conn.getInputStream())).getAsJsonObject();
        String id = obj.get("id").getAsString();

        return UUID.fromString(id.replaceFirst(
                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                "$1-$2-$3-$4-$5"
        ));
    }
}
