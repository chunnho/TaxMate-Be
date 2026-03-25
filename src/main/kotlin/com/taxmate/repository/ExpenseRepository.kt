package com.taxmate.repository

import com.taxmate.entity.ExpenseCategory
import com.taxmate.entity.ExpenseEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface ExpenseRepository : JpaRepository<ExpenseEntity, String> {

    fun findByMemberIdAndDeletedAtIsNullOrderByExpenseDateDesc(
        memberId: String,
    ): List<ExpenseEntity>

    fun findByMemberIdAndExpenseDateBetweenAndDeletedAtIsNullOrderByExpenseDateDesc(
        memberId: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<ExpenseEntity>

    fun findByMemberIdAndCategoryAndDeletedAtIsNullOrderByExpenseDateDesc(
        memberId: String,
        category: ExpenseCategory,
    ): List<ExpenseEntity>

    fun findByIdAndMemberIdAndDeletedAtIsNull(
        id: String,
        memberId: String,
    ): ExpenseEntity?
}
