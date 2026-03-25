package com.taxmate.entity

import com.taxmate.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "members")
class MemberEntity(

    @Column(name = "email", nullable = false, unique = true)
    var email: String = "",

    @Column(name = "password", nullable = false)
    var password: String = "",

    @Column(name = "nickname", nullable = false, length = 50)
    var nickname: String = "",

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 20)
    var provider: AuthProvider = AuthProvider.LOCAL,

) : BaseEntity()

enum class AuthProvider {
    LOCAL, KAKAO, GOOGLE, APPLE
}
