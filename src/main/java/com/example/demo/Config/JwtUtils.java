package com.example.demo.Config;

import org.springframework.security.oauth2.jwt.Jwt;
import java.util.List;
import java.util.Map;

public class JwtUtils {
    public static List<String> extractRoles(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        return (List<String>) claims.getOrDefault("https://tu-api/roles", List.of());
    }
}

