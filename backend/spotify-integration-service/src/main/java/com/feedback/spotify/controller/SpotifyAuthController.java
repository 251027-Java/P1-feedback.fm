package com.feedback.spotify.controller;

import com.feedback.spotify.service.SpotifyAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class SpotifyAuthController {
    private final SpotifyAuthService authService;

    public SpotifyAuthController(SpotifyAuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> getAuthUrl() {
        String url = authService.getAuthorizationUrl();
        return ResponseEntity.ok(Map.of("authUrl", url));
    }

    @GetMapping("/callback")
    public RedirectView callback(@RequestParam String code) {
        Map<String, Object> tokenResp = authService.exchangeCodeForToken(code);
        String accessToken = tokenResp.getOrDefault("access_token", "").toString();
        String refreshToken = tokenResp.getOrDefault("refresh_token", "").toString();
        // For now redirect to frontend with tokens in query (frontend should handle securely)
        String redirect = authService.getFrontendUrl() + "?code=" + URLEncoder.encode(code, StandardCharsets.UTF_8)
                + "&spotifyToken=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8)
                + "&refreshToken=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);
        return new RedirectView(redirect);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) return ResponseEntity.badRequest().body(Map.of("error","refreshToken required"));
        Map<String, Object> resp = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(resp);
    }
}
