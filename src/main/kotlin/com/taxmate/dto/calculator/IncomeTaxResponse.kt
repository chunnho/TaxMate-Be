package com.taxmate.dto.calculator

import java.math.BigDecimal

data class IncomeTaxResponse(
    val annualIncome: BigDecimal,
    val totalExpenses: BigDecimal,
    val incomeAmount: BigDecimal,
    val taxableIncome: BigDecimal,
    val calculatedTax: BigDecimal,
    val localIncomeTax: BigDecimal,
    val totalTax: BigDecimal,
    val alreadyPaidTax: BigDecimal,
    val estimatedRefundOrPayment: BigDecimal,
    val isRefund: Boolean,
    val appliedTaxRate: String,
    val basicDeduction: BigDecimal,
)
