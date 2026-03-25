package com.taxmate.dto.calculator

import java.math.BigDecimal

data class WithholdingTaxResponse(
    val grossAmount: BigDecimal,
    val incomeTax: BigDecimal,
    val localTax: BigDecimal,
    val totalTax: BigDecimal,
    val netAmount: BigDecimal,
)
