package com.taxmate.dto.income

import com.taxmate.entity.IncomeEntity
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime

data class IncomeResponse(
    val id: String,
    val clientName: String,
    val amount: BigDecimal,
    val withholdingTax: BigDecimal,
    val netAmount: BigDecimal,
    val incomeDate: LocalDate,
    val memo: String?,
    val createdAt: LocalDateTime?,
) {
    companion object {
        private val WITHHOLDING_RATE = BigDecimal("0.033")

        @JvmStatic
        fun from(entity: IncomeEntity): IncomeResponse {
            val withholdingTax = entity.amount.multiply(WITHHOLDING_RATE)
                .setScale(0, RoundingMode.FLOOR)
            val netAmount = entity.amount.subtract(withholdingTax)

            return IncomeResponse(
                id = entity.id,
                clientName = entity.clientName,
                amount = entity.amount,
                withholdingTax = withholdingTax,
                netAmount = netAmount,
                incomeDate = entity.incomeDate,
                memo = entity.memo,
                createdAt = entity.createdAt,
            )
        }
    }
}
