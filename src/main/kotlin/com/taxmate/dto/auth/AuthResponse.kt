package com.taxmate.dto.auth

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val memberId: String,
    val email: String,
    val nickname: String,
)
