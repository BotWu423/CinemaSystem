package com.example.movietheatersystem.tool;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class JwtKeyGenerator {
    public static void main(String[] args) {
        // 自动生成一个适用于 HS512 的 SecretKey（长度 >= 512 bits）
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String base64Key = java.util.Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Generated Base64 Secret Key for HS512:");
        System.out.println(base64Key);
    }
}