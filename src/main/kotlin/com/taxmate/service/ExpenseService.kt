package com.taxmate.service

import com.taxmate.common.exception.ApiException
import com.taxmate.common.exception.ErrorCode
import com.taxmate.dto.expense.CreateExpenseRequest
import com.taxmate.dto.expense.ExpenseResponse
import com.taxmate.dto.expense.UpdateExpenseRequest
import com.taxmate.entity.ExpenseCategory
import com.taxmate.entity.ExpenseEntity
import com.taxmate.repository.ExpenseRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class ExpenseService(
    private val expenseRepository: ExpenseRepository,
) {

    fun createExpense(memberId: String, request: CreateExpenseRequest): ExpenseResponse {
        val expense = ExpenseEntity(
            memberId = memberId,
            title = request.title!!,
            amount = request.amount!!,
            expenseDate = request.expenseDate!!,
            category = request.category!!,
            memo = request.memo,
            isDeductible = request.isDeductible,
        )
        this.expenseRepository.save(expense)
        return ExpenseResponse.from(expense)
    }

    @Transactional(readOnly = true)
    fun getExpenses(
        memberId: String,
        year: Int?,
        month: Int?,
        category: ExpenseCategory?,
    ): List<ExpenseResponse> {
        val expenses = when {
            category != null -> {
                this.expenseRepository.findByMemberIdAndCategoryAndDeletedAtIsNullOrderByExpenseDateDesc(
                    memberId = memberId,
                    category = category,
                )
            }
            year != null && month != null -> {
                val startDate = LocalDate.of(year, month, 1)
                val endDate = startDate.withDayOfMonth(startDate.lengthOfMonth())
                this.expenseRepository.findByMemberIdAndExpenseDateBetweenAndDeletedAtIsNullOrderByExpenseDateDesc(
                    memberId = memberId,
                    startDate = startDate,
                    endDate = endDate,
                )
            }
            else -> {
                this.expenseRepository.findByMemberIdAndDeletedAtIsNullOrderByExpenseDateDesc(memberId)
            }
        }
        return expenses.map { ExpenseResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun getExpense(memberId: String, expenseId: String): ExpenseResponse {
        val expense = this.findExpenseOrThrow(memberId, expenseId)
        return ExpenseResponse.from(expense)
    }

    fun updateExpense(memberId: String, expenseId: String, request: UpdateExpenseRequest): ExpenseResponse {
        val expense = this.findExpenseOrThrow(memberId, expenseId)
        expense.title = request.title!!
        expense.amount = request.amount!!
        expense.expenseDate = request.expenseDate!!
        expense.category = request.category!!
        expense.memo = request.memo
        expense.isDeductible = request.isDeductible
        this.expenseRepository.save(expense)
        return ExpenseResponse.from(expense)
    }

    fun deleteExpense(memberId: String, expenseId: String) {
        val expense = this.findExpenseOrThrow(memberId, expenseId)
        expense.delete()
        this.expenseRepository.save(expense)
    }

    private fun findExpenseOrThrow(memberId: String, expenseId: String): ExpenseEntity {
        return this.expenseRepository.findByIdAndMemberIdAndDeletedAtIsNull(
            id = expenseId,
            memberId = memberId,
        ) ?: throw ApiException(ErrorCode.EXPENSE_NOT_FOUND)
    }
}
