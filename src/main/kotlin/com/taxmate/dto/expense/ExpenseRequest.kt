package com.taxmate.dto.expense

import com.taxmate.entity.ExpenseCategory
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate

data class CreateExpenseRequest(
    @field:NotBlank(message = "항목명은 필수입니다.")
    val title: String? = null,

    @field:NotNull(message = "금액은 필수입니다.")
    @field:Positive(message = "금액은 0보다 커야 합니다.")
    val amount: BigDecimal? = null,

    @field:NotNull(message = "지출 날짜는 필수입니다.")
    val expenseDate: LocalDate? = null,

    @field:NotNull(message = "카테고리는 필수입니다.")
    val category: ExpenseCategory? = null,

    val memo: String? = null,

    val isDeductible: Boolean = true,
)

data class UpdateExpenseRequest(
    @field:NotBlank(message = "항목명은 필수입니다.")
    val title: String? = null,

    @field:NotNull(message = "금액은 필수입니다.")
    @field:Positive(message = "금액은 0보다 커야 합니다.")
    val amount: BigDecimal? = null,

    @field:NotNull(message = "지출 날짜는 필수입니다.")
    val expenseDate: LocalDate? = null,

    @field:NotNull(message = "카테고리는 필수입니다.")
    val category: ExpenseCategory? = null,

    val memo: String? = null,

    val isDeductible: Boolean = true,
)
