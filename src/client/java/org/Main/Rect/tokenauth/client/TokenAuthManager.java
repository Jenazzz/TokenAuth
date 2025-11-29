package org.Main.Rect.tokenauth.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * TokenAuth Manager
 * Ported to 1.21.5
 * Author: Rect
 */
public class TokenAuthManager {
    private static Session originalSession = null;
    
    public static void saveOriginalSession() {
        if (originalSession == null) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.getSession() != null) {
                originalSession = client.getSession();
            }
        }
    }
    
    public static Session getOriginalSession() {
        return originalSession;
    }
    
    public static SessionInfo authenticateWithToken(String token) throws Exception {
        // Parse token if it's in format "username:uuid:token"
        if (token.contains(":")) {
            String[] parts = token.split(":");
            if (parts.length == 3) {
                return new SessionInfo(parts[0], parts[1], parts[2]);
            }
        }
        
        // If only token provided, fetch profile from API
        HttpURLConnection connection = (HttpURLConnection) new URL("https://api.minecraftservices.com/minecraft/profile").openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestMethod("GET");
        
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            connection.disconnect();
            throw new Exception("Failed to authenticate: HTTP " + responseCode);
        }
        
        String response;
        try (InputStream inputStream = connection.getInputStream();
             Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            response = scanner.useDelimiter("\\A").next();
        } finally {
            connection.disconnect();
        }
        
        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        String username = json.get("name").getAsString();
        String uuid = json.get("id").getAsString();
        
        return new SessionInfo(username, uuid, token);
    }
    
    public static class SessionInfo {
        public final String username;
        public final String uuid;
        public final String token;
        
        public SessionInfo(String username, String uuid, String token) {
            this.username = username;
            this.uuid = uuid;
            this.token = token;
        }
    }
}

