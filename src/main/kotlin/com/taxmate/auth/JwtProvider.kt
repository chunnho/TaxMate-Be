package com.taxmate.auth

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtProvider(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.access-token-expiry}") private val accessTokenExpiry: Long,
    @Value("\${jwt.refresh-token-expiry}") private val refreshTokenExpiry: Long,
) {
    private val key by lazy { Keys.hmacShaKeyFor(secret.toByteArray()) }

    fun createAccessToken(memberId: String): String {
        return this.createToken(memberId, accessTokenExpiry)
    }

    fun createRefreshToken(memberId: String): String {
        return this.createToken(memberId, refreshTokenExpiry)
    }

    fun getMemberIdFromToken(token: String): String? {
        return try {
            Jwts.parser()
                .verifyWith(this.key)
                .build()
                .parseSignedClaims(token)
                .payload
                .subject
        } catch (e: ExpiredJwtException) {
            null
        } catch (e: Exception) {
            null
        }
    }

    fun isTokenValid(token: String): Boolean {
        return this.getMemberIdFromToken(token) != null
    }

    private fun createToken(memberId: String, expiry: Long): String {
        val now = Date()
        return Jwts.builder()
            .subject(memberId)
            .issuedAt(now)
            .expiration(Date(now.time + expiry))
            .signWith(this.key)
            .compact()
    }
}
