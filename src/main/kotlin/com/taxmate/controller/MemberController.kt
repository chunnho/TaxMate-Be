package com.taxmate.controller

import com.taxmate.common.api.ApiResponse
import com.taxmate.common.exception.ApiException
import com.taxmate.common.exception.ErrorCode
import com.taxmate.dto.member.MemberResponse
import com.taxmate.repository.MemberRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "회원", description = "내 정보 조회/수정")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberRepository: MemberRepository,
) {

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 회원의 정보를 조회합니다.")
    @GetMapping("/me")
    fun getMe(): ApiResponse<MemberResponse> {
        val memberId = SecurityContextHolder.getContext().authentication.principal as String
        val member = this.memberRepository.findById(memberId)
            .orElseThrow { ApiException(ErrorCode.MEMBER_NOT_FOUND) }

        return ApiResponse.ok(MemberResponse.from(member))
    }
}
