package com.example.Intern_api.auth;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 86400000;

    public String generateToken(Long userId, String userName, Set<String> roles) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))  // User ID làm subject
                .claim("userName", userName)         // Lưu tên user vào claim
                .claim("roles", new ArrayList<>(roles)) // Lưu danh sách quyền
                .setIssuedAt(new Date()) //  Thời điểm phát hành
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //  Thời điểm hết hạn
                .signWith(key, SignatureAlgorithm.HS256) //  Chỉ định thuật toán ký
                .compact();
    }

    public List<String> extractRoles(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles", List.class);
    }
    
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userName", String.class); // Lấy từ claim "username"
    }

    public Long extractUserId(String token) {
        String subject = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Long.parseLong(subject); // Chuyển subject về kiểu long
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true; // Token hợp lệ
        } catch (ExpiredJwtException e) {
            System.err.println("Token đã hết hạn: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("Token không đúng định dạng: " + e.getMessage());
        } catch (SignatureException e) {
            System.err.println("Chữ ký không hợp lệ: " + e.getMessage());
        } catch (JwtException e) {
            System.err.println("Token không hợp lệ: " + e.getMessage());
        }
        return false; // Token không hợp lệ
    }
}
