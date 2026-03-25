package com.taxmate.repository

import com.taxmate.entity.IncomeEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface IncomeRepository : JpaRepository<IncomeEntity, String> {

    fun findByMemberIdAndDeletedAtIsNullOrderByIncomeDateDesc(
        memberId: String,
    ): List<IncomeEntity>

    fun findByMemberIdAndIncomeDateBetweenAndDeletedAtIsNullOrderByIncomeDateDesc(
        memberId: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<IncomeEntity>

    fun findByIdAndMemberIdAndDeletedAtIsNull(
        id: String,
        memberId: String,
    ): IncomeEntity?
}
