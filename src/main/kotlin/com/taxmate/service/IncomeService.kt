package com.taxmate.service

import com.taxmate.common.exception.ApiException
import com.taxmate.common.exception.ErrorCode
import com.taxmate.dto.income.CreateIncomeRequest
import com.taxmate.dto.income.IncomeResponse
import com.taxmate.dto.income.UpdateIncomeRequest
import com.taxmate.entity.IncomeEntity
import com.taxmate.repository.IncomeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class IncomeService(
    private val incomeRepository: IncomeRepository,
) {

    fun createIncome(memberId: String, request: CreateIncomeRequest): IncomeResponse {
        val income = IncomeEntity(
            memberId = memberId,
            clientName = request.clientName!!,
            amount = request.amount!!,
            incomeDate = request.incomeDate!!,
            memo = request.memo,
        )
        this.incomeRepository.save(income)
        return IncomeResponse.from(income)
    }

    @Transactional(readOnly = true)
    fun getIncomes(memberId: String, year: Int?, month: Int?): List<IncomeResponse> {
        val incomes = if (year != null && month != null) {
            val startDate = LocalDate.of(year, month, 1)
            val endDate = startDate.withDayOfMonth(startDate.lengthOfMonth())
            this.incomeRepository.findByMemberIdAndIncomeDateBetweenAndDeletedAtIsNullOrderByIncomeDateDesc(
                memberId = memberId,
                startDate = startDate,
                endDate = endDate,
            )
        } else {
            this.incomeRepository.findByMemberIdAndDeletedAtIsNullOrderByIncomeDateDesc(memberId)
        }
        return incomes.map { IncomeResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun getIncome(memberId: String, incomeId: String): IncomeResponse {
        val income = this.findIncomeOrThrow(memberId, incomeId)
        return IncomeResponse.from(income)
    }

    fun updateIncome(memberId: String, incomeId: String, request: UpdateIncomeRequest): IncomeResponse {
        val income = this.findIncomeOrThrow(memberId, incomeId)
        income.clientName = request.clientName!!
        income.amount = request.amount!!
        income.incomeDate = request.incomeDate!!
        income.memo = request.memo
        this.incomeRepository.save(income)
        return IncomeResponse.from(income)
    }

    fun deleteIncome(memberId: String, incomeId: String) {
        val income = this.findIncomeOrThrow(memberId, incomeId)
        income.delete()
        this.incomeRepository.save(income)
    }

    private fun findIncomeOrThrow(memberId: String, incomeId: String): IncomeEntity {
        return this.incomeRepository.findByIdAndMemberIdAndDeletedAtIsNull(
            id = incomeId,
            memberId = memberId,
        ) ?: throw ApiException(ErrorCode.INCOME_NOT_FOUND)
    }
}
