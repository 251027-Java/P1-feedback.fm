package com.feedback.spotify.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class SpotifyAuthService {

    @Value("${spotify.clientId:}")
    private String clientId;

    @Value("${spotify.clientSecret:}")
    private String clientSecret;

    @Value("${spotify.redirectUri:http://localhost:3000}")
    private String redirectUri;

    @Value("${frontend.url:http://localhost:3000}")
    private String frontendUrl;

    public String getAuthorizationUrl() {
        String scopes = "user-read-recently-played user-top-read user-read-private";
        String url = "https://accounts.spotify.com/authorize?response_type=code&client_id=" + clientId
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&scope=" + URLEncoder.encode(scopes, StandardCharsets.UTF_8);
        return url;
    }

    public Map<String,Object> exchangeCodeForToken(String code) {
        // Placeholder: implement proper token exchange with Spotify Web API
        Map<String,Object> m = new HashMap<>();
        m.put("access_token", "mock-access-token");
        m.put("refresh_token", "mock-refresh-token");
        m.put("expires_in", 3600);
        return m;
    }

    public Map<String,Object> refreshToken(String refreshToken) {
        // Placeholder: implement proper refresh
        Map<String,Object> m = new HashMap<>();
        m.put("access_token", "mock-refreshed-token");
        m.put("expires_in", 3600);
        return m;
    }

    public String getFrontendUrl() {
        return frontendUrl;
    }
}
