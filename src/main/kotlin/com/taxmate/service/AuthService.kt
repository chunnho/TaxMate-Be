package com.taxmate.service

import com.taxmate.auth.JwtProvider
import com.taxmate.common.exception.ApiException
import com.taxmate.common.exception.ErrorCode
import com.taxmate.dto.auth.AuthResponse
import com.taxmate.dto.auth.LoginRequest
import com.taxmate.dto.auth.RefreshRequest
import com.taxmate.dto.auth.SignupRequest
import com.taxmate.entity.AuthProvider
import com.taxmate.entity.MemberEntity
import com.taxmate.repository.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider,
) {

    fun signup(request: SignupRequest): AuthResponse {
        if (this.memberRepository.existsByEmailAndDeletedAtIsNull(request.email!!)) {
            throw ApiException(ErrorCode.DUPLICATE_EMAIL)
        }

        val member = MemberEntity(
            email = request.email,
            password = this.passwordEncoder.encode(request.password),
            nickname = request.nickname!!,
            provider = AuthProvider.LOCAL,
        )

        this.memberRepository.save(member)

        return this.createAuthResponse(member)
    }

    fun login(request: LoginRequest): AuthResponse {
        val member = this.memberRepository.findByEmailAndDeletedAtIsNull(request.email!!)
            ?: throw ApiException(ErrorCode.MEMBER_NOT_FOUND)

        if (!this.passwordEncoder.matches(request.password, member.password)) {
            throw ApiException(ErrorCode.INVALID_PASSWORD)
        }

        return this.createAuthResponse(member)
    }

    fun refresh(request: RefreshRequest): AuthResponse {
        val memberId = this.jwtProvider.getMemberIdFromToken(request.refreshToken!!)
            ?: throw ApiException(ErrorCode.INVALID_TOKEN)

        val member = this.memberRepository.findById(memberId)
            .orElseThrow { ApiException(ErrorCode.MEMBER_NOT_FOUND) }

        if (member.isDeleted()) {
            throw ApiException(ErrorCode.MEMBER_NOT_FOUND)
        }

        return this.createAuthResponse(member)
    }

    private fun createAuthResponse(member: MemberEntity): AuthResponse {
        return AuthResponse(
            accessToken = this.jwtProvider.createAccessToken(member.id),
            refreshToken = this.jwtProvider.createRefreshToken(member.id),
            memberId = member.id,
            email = member.email,
            nickname = member.nickname,
        )
    }
}
