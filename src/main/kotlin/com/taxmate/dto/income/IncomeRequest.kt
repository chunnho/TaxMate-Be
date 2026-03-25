package com.taxmate.dto.income

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate

data class CreateIncomeRequest(
    @field:NotBlank(message = "클라이언트명은 필수입니다.")
    val clientName: String? = null,

    @field:NotNull(message = "금액은 필수입니다.")
    @field:Positive(message = "금액은 0보다 커야 합니다.")
    val amount: BigDecimal? = null,

    @field:NotNull(message = "수입 날짜는 필수입니다.")
    val incomeDate: LocalDate? = null,

    val memo: String? = null,
)

data class UpdateIncomeRequest(
    @field:NotBlank(message = "클라이언트명은 필수입니다.")
    val clientName: String? = null,

    @field:NotNull(message = "금액은 필수입니다.")
    @field:Positive(message = "금액은 0보다 커야 합니다.")
    val amount: BigDecimal? = null,

    @field:NotNull(message = "수입 날짜는 필수입니다.")
    val incomeDate: LocalDate? = null,

    val memo: String? = null,
)
