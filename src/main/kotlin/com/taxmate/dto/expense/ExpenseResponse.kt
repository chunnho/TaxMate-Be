package com.taxmate.dto.expense

import com.taxmate.entity.ExpenseCategory
import com.taxmate.entity.ExpenseEntity
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class ExpenseResponse(
    val id: String,
    val title: String,
    val amount: BigDecimal,
    val expenseDate: LocalDate,
    val category: ExpenseCategory,
    val categoryDisplayName: String,
    val memo: String?,
    val isDeductible: Boolean,
    val createdAt: LocalDateTime?,
) {
    companion object {
        @JvmStatic
        fun from(entity: ExpenseEntity): ExpenseResponse {
            return ExpenseResponse(
                id = entity.id,
                title = entity.title,
                amount = entity.amount,
                expenseDate = entity.expenseDate,
                category = entity.category,
                categoryDisplayName = entity.category.displayName,
                memo = entity.memo,
                isDeductible = entity.isDeductible,
                createdAt = entity.createdAt,
            )
        }
    }
}
