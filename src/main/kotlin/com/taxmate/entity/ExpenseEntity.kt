package com.taxmate.entity

import com.taxmate.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "expenses")
class ExpenseEntity(

    @Column(name = "member_id", nullable = false, length = 36)
    var memberId: String = "",

    @Column(name = "title", nullable = false, length = 200)
    var title: String = "",

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    var amount: BigDecimal = BigDecimal.ZERO,

    @Column(name = "expense_date", nullable = false)
    var expenseDate: LocalDate = LocalDate.now(),

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    var category: ExpenseCategory = ExpenseCategory.ETC,

    @Column(name = "memo", length = 500)
    var memo: String? = null,

    @Column(name = "is_deductible", nullable = false)
    var isDeductible: Boolean = true,

) : BaseEntity()

enum class ExpenseCategory(
    val displayName: String,
) {
    OFFICE_SUPPLY("사무용품"),
    EQUIPMENT("장비"),
    SOFTWARE("소프트웨어"),
    TRANSPORT("교통비"),
    MEAL("식비"),
    EDUCATION("교육"),
    TELECOM("통신비"),
    RENT("임대료"),
    OUTSOURCING("외주비"),
    TAX("세금/공과금"),
    INSURANCE("보험"),
    ETC("기타"),
}
