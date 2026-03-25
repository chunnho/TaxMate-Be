package com.taxmate.entity

import com.taxmate.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "incomes")
class IncomeEntity(

    @Column(name = "member_id", nullable = false, length = 36)
    var memberId: String = "",

    @Column(name = "client_name", nullable = false, length = 100)
    var clientName: String = "",

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    var amount: BigDecimal = BigDecimal.ZERO,

    @Column(name = "income_date", nullable = false)
    var incomeDate: LocalDate = LocalDate.now(),

    @Column(name = "memo", length = 500)
    var memo: String? = null,

) : BaseEntity()
