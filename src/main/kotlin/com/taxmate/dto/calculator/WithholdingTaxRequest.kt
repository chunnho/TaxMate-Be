package com.taxmate.dto.calculator

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class WithholdingTaxRequest(

    @field:NotNull(message = "금액은 필수입니다.")
    @field:Positive(message = "금액은 0보다 커야 합니다.")
    val amount: BigDecimal?,
)
