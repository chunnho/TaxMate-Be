package com.taxmate.dto.calculator

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal

data class IncomeTaxRequest(

    @field:NotNull(message = "연간 총수입은 필수입니다.")
    @field:Positive(message = "연간 총수입은 0보다 커야 합니다.")
    val annualIncome: BigDecimal?,

    @field:PositiveOrZero(message = "경비는 0 이상이어야 합니다.")
    val expenses: BigDecimal? = BigDecimal.ZERO,

    val expenseRate: BigDecimal? = null,
)
