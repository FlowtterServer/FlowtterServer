package com.example.flowterserver.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService {

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(
        "FlowterSuperSecretKeyForJwt2026ChangeThisLater123456789".toByteArray(
            StandardCharsets.UTF_8
        )
    )

    private val expirationTime = 1000L * 60 * 60 * 24

    fun generateToken(username: String): String {
        return Jwts.builder()
            .subject(username)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(secretKey)
            .compact()
    }

    fun extractUsername(token: String): String {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }

    fun isTokenValid(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)

            true
        } catch (exception: Exception) {
            false
        }
    }
}