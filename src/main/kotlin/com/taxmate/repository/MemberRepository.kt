package com.taxmate.repository

import com.taxmate.entity.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<MemberEntity, String> {

    fun findByEmailAndDeletedAtIsNull(email: String): MemberEntity?

    fun existsByEmailAndDeletedAtIsNull(email: String): Boolean
}
