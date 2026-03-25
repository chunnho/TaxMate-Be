package com.taxmate.controller

import com.taxmate.common.api.ApiResponse
import com.taxmate.dto.auth.AuthResponse
import com.taxmate.dto.auth.LoginRequest
import com.taxmate.dto.auth.RefreshRequest
import com.taxmate.dto.auth.SignupRequest
import com.taxmate.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Tag(name = "인증", description = "회원가입, 로그인, 토큰 갱신")
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {

    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 닉네임으로 회원가입합니다.")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(
        @RequestBody @Valid request: SignupRequest,
    ): ApiResponse<AuthResponse> {
        val result = this.authService.signup(request)
        return ApiResponse.created(result)
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @PostMapping("/login")
    fun login(
        @RequestBody @Valid request: LoginRequest,
    ): ApiResponse<AuthResponse> {
        val result = this.authService.login(request)
        return ApiResponse.ok(result)
    }

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰으로 새 액세스 토큰을 발급합니다.")
    @PostMapping("/refresh")
    fun refresh(
        @RequestBody @Valid request: RefreshRequest,
    ): ApiResponse<AuthResponse> {
        val result = this.authService.refresh(request)
        return ApiResponse.ok(result)
    }
}
