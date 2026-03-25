package com.taxmate.dto.dashboard

import java.math.BigDecimal

data class MonthlySummaryResponse(
    val year: Int,
    val month: Int,
    val totalIncome: BigDecimal,
    val totalExpense: BigDecimal,
    val totalWithholdingTax: BigDecimal,
    val netProfit: BigDecimal,
    val incomeCount: Int,
    val expenseCount: Int,
)

data class YearlySummaryResponse(
    val year: Int,
    val totalIncome: BigDecimal,
    val totalExpense: BigDecimal,
    val totalDeductibleExpense: BigDecimal,
    val totalWithholdingTax: BigDecimal,
    val taxableIncome: BigDecimal,
    val estimatedIncomeTax: BigDecimal,
    val estimatedLocalTax: BigDecimal,
    val estimatedTotalTax: BigDecimal,
    val alreadyPaidTax: BigDecimal,
    val estimatedRefundOrPayment: BigDecimal,
    val isRefund: Boolean,
    val monthlyBreakdown: List<MonthlyBreakdownItem>,
)

data class MonthlyBreakdownItem(
    val month: Int,
    val income: BigDecimal,
    val expense: BigDecimal,
)
