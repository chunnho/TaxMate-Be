package com.taxmate.dto.member

import com.taxmate.entity.MemberEntity
import java.time.LocalDateTime

data class MemberResponse(
    val id: String,
    val email: String,
    val nickname: String,
    val provider: String,
    val createdAt: LocalDateTime?,
) {
    companion object {
        fun from(entity: MemberEntity): MemberResponse {
            return MemberResponse(
                id = entity.id,
                email = entity.email,
                nickname = entity.nickname,
                provider = entity.provider.name,
                createdAt = entity.createdAt,
            )
        }
    }
}
