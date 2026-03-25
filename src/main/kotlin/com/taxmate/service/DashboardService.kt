package com.taxmate.service

import com.taxmate.dto.dashboard.MonthlyBreakdownItem
import com.taxmate.dto.dashboard.MonthlySummaryResponse
import com.taxmate.dto.dashboard.YearlySummaryResponse
import com.taxmate.repository.ExpenseRepository
import com.taxmate.repository.IncomeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class DashboardService(
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository,
) {

    companion object {
        private val WITHHOLDING_RATE = BigDecimal("0.033")

        private val TAX_BRACKETS = listOf(
            TaxBracket(BigDecimal("14000000"), BigDecimal("0.06"), BigDecimal.ZERO),
            TaxBracket(BigDecimal("50000000"), BigDecimal("0.15"), BigDecimal("840000")),
            TaxBracket(BigDecimal("88000000"), BigDecimal("0.24"), BigDecimal("6240000")),
            TaxBracket(BigDecimal("150000000"), BigDecimal("0.35"), BigDecimal("15360000")),
            TaxBracket(BigDecimal("300000000"), BigDecimal("0.38"), BigDecimal("19400000")),
            TaxBracket(BigDecimal("500000000"), BigDecimal("0.40"), BigDecimal("25400000")),
            TaxBracket(BigDecimal("1000000000"), BigDecimal("0.42"), BigDecimal("35400000")),
            TaxBracket(BigDecimal.valueOf(Long.MAX_VALUE), BigDecimal("0.45"), BigDecimal("65400000")),
        )
    }

    fun getMonthlySummary(memberId: String, year: Int, month: Int): MonthlySummaryResponse {
        val startDate = LocalDate.of(year, month, 1)
        val endDate = startDate.withDayOfMonth(startDate.lengthOfMonth())

        val incomes = this.incomeRepository
            .findByMemberIdAndIncomeDateBetweenAndDeletedAtIsNullOrderByIncomeDateDesc(
                memberId = memberId,
                startDate = startDate,
                endDate = endDate,
            )

        val expenses = this.expenseRepository
            .findByMemberIdAndExpenseDateBetweenAndDeletedAtIsNullOrderByExpenseDateDesc(
                memberId = memberId,
                startDate = startDate,
                endDate = endDate,
            )

        val totalIncome = incomes.sumOf { it.amount }
        val totalExpense = expenses.sumOf { it.amount }
        val totalWithholdingTax = totalIncome.multiply(WITHHOLDING_RATE)
            .setScale(0, RoundingMode.FLOOR)
        val netProfit = totalIncome.subtract(totalWithholdingTax).subtract(totalExpense)

        return MonthlySummaryResponse(
            year = year,
            month = month,
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            totalWithholdingTax = totalWithholdingTax,
            netProfit = netProfit,
            incomeCount = incomes.size,
            expenseCount = expenses.size,
        )
    }

    fun getYearlySummary(memberId: String, year: Int): YearlySummaryResponse {
        val startDate = LocalDate.of(year, 1, 1)
        val endDate = LocalDate.of(year, 12, 31)

        val incomes = this.incomeRepository
            .findByMemberIdAndIncomeDateBetweenAndDeletedAtIsNullOrderByIncomeDateDesc(
                memberId = memberId,
                startDate = startDate,
                endDate = endDate,
            )

        val expenses = this.expenseRepository
            .findByMemberIdAndExpenseDateBetweenAndDeletedAtIsNullOrderByExpenseDateDesc(
                memberId = memberId,
                startDate = startDate,
                endDate = endDate,
            )

        val totalIncome = incomes.sumOf { it.amount }
        val totalExpense = expenses.sumOf { it.amount }
        val totalDeductibleExpense = expenses
            .filter { it.isDeductible }
            .sumOf { it.amount }

        val totalWithholdingTax = totalIncome.multiply(WITHHOLDING_RATE)
            .setScale(0, RoundingMode.FLOOR)

        // 과세표준 = 총수입 - 경비
        val taxableIncome = totalIncome.subtract(totalDeductibleExpense).max(BigDecimal.ZERO)

        // 종소세 계산
        val estimatedIncomeTax = this.calculateIncomeTax(taxableIncome)
        val estimatedLocalTax = estimatedIncomeTax.multiply(BigDecimal("0.1"))
            .setScale(0, RoundingMode.FLOOR)
        val estimatedTotalTax = estimatedIncomeTax.add(estimatedLocalTax)

        // 환급/추가납부 = 이미 낸 세금 - 예상 세금
        val estimatedRefundOrPayment = totalWithholdingTax.subtract(estimatedTotalTax)

        // 월별 브레이크다운
        val monthlyBreakdown = (1..12).map { month ->
            val monthStart = LocalDate.of(year, month, 1)
            val monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth())

            MonthlyBreakdownItem(
                month = month,
                income = incomes
                    .filter { !it.incomeDate.isBefore(monthStart) && !it.incomeDate.isAfter(monthEnd) }
                    .sumOf { it.amount },
                expense = expenses
                    .filter { !it.expenseDate.isBefore(monthStart) && !it.expenseDate.isAfter(monthEnd) }
                    .sumOf { it.amount },
            )
        }

        return YearlySummaryResponse(
            year = year,
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            totalDeductibleExpense = totalDeductibleExpense,
            totalWithholdingTax = totalWithholdingTax,
            taxableIncome = taxableIncome,
            estimatedIncomeTax = estimatedIncomeTax,
            estimatedLocalTax = estimatedLocalTax,
            estimatedTotalTax = estimatedTotalTax,
            alreadyPaidTax = totalWithholdingTax,
            estimatedRefundOrPayment = estimatedRefundOrPayment.abs(),
            isRefund = estimatedRefundOrPayment.signum() >= 0,
            monthlyBreakdown = monthlyBreakdown,
        )
    }

    private fun calculateIncomeTax(taxableIncome: BigDecimal): BigDecimal {
        if (taxableIncome.signum() <= 0) return BigDecimal.ZERO

        var previousLimit = BigDecimal.ZERO
        for (bracket in TAX_BRACKETS) {
            if (taxableIncome <= bracket.limit) {
                return taxableIncome.subtract(previousLimit)
                    .multiply(bracket.rate)
                    .add(bracket.cumulativeTax)
                    .setScale(0, RoundingMode.FLOOR)
            }
            previousLimit = bracket.limit
        }
        return BigDecimal.ZERO
    }

    private data class TaxBracket(
        val limit: BigDecimal,
        val rate: BigDecimal,
        val cumulativeTax: BigDecimal,
    )
}
