package com.taxmate.service

import com.taxmate.dto.calculator.IncomeTaxRequest
import com.taxmate.dto.calculator.IncomeTaxResponse
import com.taxmate.dto.calculator.WithholdingTaxRequest
import com.taxmate.dto.calculator.WithholdingTaxResponse
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class TaxCalculatorService {

    companion object {
        private val INCOME_TAX_RATE = BigDecimal("0.03")
        private val LOCAL_TAX_RATE = BigDecimal("0.003")
        private val TOTAL_WITHHOLDING_RATE = BigDecimal("0.033")
        private val DEFAULT_EXPENSE_RATE = BigDecimal("0.60")
        private val BASIC_DEDUCTION = BigDecimal("1500000")
        private val LOCAL_INCOME_TAX_RATE = BigDecimal("0.10")

        private val TAX_BRACKETS = listOf(
            TaxBracket(BigDecimal("0"), BigDecimal("14000000"), BigDecimal("0.06"), BigDecimal("0")),
            TaxBracket(BigDecimal("14000000"), BigDecimal("50000000"), BigDecimal("0.15"), BigDecimal("1260000")),
            TaxBracket(BigDecimal("50000000"), BigDecimal("88000000"), BigDecimal("0.24"), BigDecimal("5760000")),
            TaxBracket(BigDecimal("88000000"), BigDecimal("150000000"), BigDecimal("0.35"), BigDecimal("15440000")),
            TaxBracket(BigDecimal("150000000"), BigDecimal("300000000"), BigDecimal("0.38"), BigDecimal("19940000")),
            TaxBracket(BigDecimal("300000000"), BigDecimal("500000000"), BigDecimal("0.40"), BigDecimal("25940000")),
            TaxBracket(BigDecimal("500000000"), BigDecimal("1000000000"), BigDecimal("0.42"), BigDecimal("35940000")),
            TaxBracket(BigDecimal("1000000000"), BigDecimal.valueOf(Long.MAX_VALUE), BigDecimal("0.45"), BigDecimal("65940000")),
        )
    }

    fun calculateWithholdingTax(request: WithholdingTaxRequest): WithholdingTaxResponse {
        val amount = request.amount!!

        val incomeTax = amount.multiply(INCOME_TAX_RATE).setScale(0, RoundingMode.DOWN)
        val localTax = amount.multiply(LOCAL_TAX_RATE).setScale(0, RoundingMode.DOWN)
        val totalTax = incomeTax.add(localTax)
        val netAmount = amount.subtract(totalTax)

        return WithholdingTaxResponse(
            grossAmount = amount,
            incomeTax = incomeTax,
            localTax = localTax,
            totalTax = totalTax,
            netAmount = netAmount,
        )
    }

    fun calculateIncomeTax(request: IncomeTaxRequest): IncomeTaxResponse {
        val annualIncome = request.annualIncome!!
        val expenseRate = request.expenseRate ?: DEFAULT_EXPENSE_RATE

        val totalExpenses = if (request.expenses != null && request.expenses > BigDecimal.ZERO) {
            request.expenses
        } else {
            annualIncome.multiply(expenseRate).setScale(0, RoundingMode.DOWN)
        }

        val incomeAmount = annualIncome.subtract(totalExpenses).max(BigDecimal.ZERO)

        val taxableIncome = incomeAmount.subtract(BASIC_DEDUCTION).max(BigDecimal.ZERO)

        val bracket = TAX_BRACKETS.first { taxableIncome < it.upperLimit }
        val calculatedTax = taxableIncome.multiply(bracket.rate)
            .subtract(bracket.deduction)
            .setScale(0, RoundingMode.DOWN)
            .max(BigDecimal.ZERO)

        val localIncomeTax = calculatedTax.multiply(LOCAL_INCOME_TAX_RATE).setScale(0, RoundingMode.DOWN)
        val totalTax = calculatedTax.add(localIncomeTax)

        val alreadyPaidTax = annualIncome.multiply(TOTAL_WITHHOLDING_RATE).setScale(0, RoundingMode.DOWN)

        val refundOrPayment = alreadyPaidTax.subtract(totalTax)

        return IncomeTaxResponse(
            annualIncome = annualIncome,
            totalExpenses = totalExpenses,
            incomeAmount = incomeAmount,
            taxableIncome = taxableIncome,
            calculatedTax = calculatedTax,
            localIncomeTax = localIncomeTax,
            totalTax = totalTax,
            alreadyPaidTax = alreadyPaidTax,
            estimatedRefundOrPayment = refundOrPayment.abs(),
            isRefund = refundOrPayment >= BigDecimal.ZERO,
            appliedTaxRate = "${bracket.rate.multiply(BigDecimal("100")).stripTrailingZeros().toPlainString()}%",
            basicDeduction = BASIC_DEDUCTION,
        )
    }

    private data class TaxBracket(
        val lowerLimit: BigDecimal,
        val upperLimit: BigDecimal,
        val rate: BigDecimal,
        val deduction: BigDecimal,
    )
}
