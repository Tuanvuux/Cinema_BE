package com.example.be.Jwt;

import com.example.be.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
@Component
public class JwtUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Tạo khóa bí mật
//    private final String SECRET = "thisIsASecretKeyThatShouldBeVerySecureAndLongEnough";
//    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    // Sinh JWT
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Đặt thông tin trong payload
//                .claim("fullName", username.getFullName())
//                .claim("phone", username.getPhone())
//                .claim("birthday", username.getBirthday().toString()) // Chuyển LocalDate thành String
//                .claim("gender", username.getGender())
                .setIssuedAt(new Date()) // Ngày tạo
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Hết hạn sau 10 giờ
                .signWith(key) // Ký với khóa bí mật
                .compact();
    }

    // Lấy thông tin từ JWT
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Kiểm tra xem JWT có hết hạn hay không
    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // Kiểm tra tính hợp lệ của JWT
    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    // Lấy toàn bộ thông tin Claims
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
