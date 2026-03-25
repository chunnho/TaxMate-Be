package com.taxmate.controller

import com.taxmate.common.api.ApiResponse
import com.taxmate.dto.expense.CreateExpenseRequest
import com.taxmate.dto.expense.ExpenseResponse
import com.taxmate.dto.expense.UpdateExpenseRequest
import com.taxmate.entity.ExpenseCategory
import com.taxmate.service.ExpenseService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Tag(name = "지출 관리", description = "지출 등록, 조회, 수정, 삭제 (로그인 필요)")
@RestController
@RequestMapping("/api/expenses")
class ExpenseController(
    private val expenseService: ExpenseService,
) {

    @Operation(summary = "지출 등록", description = "새로운 지출 내역을 등록합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createExpense(
        @RequestBody @Valid request: CreateExpenseRequest,
    ): ApiResponse<ExpenseResponse> {
        val memberId = this.getCurrentMemberId()
        val result = this.expenseService.createExpense(memberId, request)
        return ApiResponse.created(result)
    }

    @Operation(summary = "지출 목록 조회", description = "내 지출 목록을 조회합니다. year/month 또는 category로 필터링 가능합니다.")
    @GetMapping
    fun getExpenses(
        @RequestParam(required = false) year: Int?,
        @RequestParam(required = false) month: Int?,
        @RequestParam(required = false) category: ExpenseCategory?,
    ): ApiResponse<List<ExpenseResponse>> {
        val memberId = this.getCurrentMemberId()
        val result = this.expenseService.getExpenses(memberId, year, month, category)
        return ApiResponse.ok(result)
    }

    @Operation(summary = "지출 상세 조회", description = "지출 내역 1건을 조회합니다.")
    @GetMapping("/{expenseId}")
    fun getExpense(
        @PathVariable expenseId: String,
    ): ApiResponse<ExpenseResponse> {
        val memberId = this.getCurrentMemberId()
        val result = this.expenseService.getExpense(memberId, expenseId)
        return ApiResponse.ok(result)
    }

    @Operation(summary = "지출 수정", description = "지출 내역을 수정합니다.")
    @PutMapping("/{expenseId}")
    fun updateExpense(
        @PathVariable expenseId: String,
        @RequestBody @Valid request: UpdateExpenseRequest,
    ): ApiResponse<ExpenseResponse> {
        val memberId = this.getCurrentMemberId()
        val result = this.expenseService.updateExpense(memberId, expenseId, request)
        return ApiResponse.ok(result)
    }

    @Operation(summary = "지출 삭제", description = "지출 내역을 삭제합니다. (소프트 삭제)")
    @DeleteMapping("/{expenseId}")
    fun deleteExpense(
        @PathVariable expenseId: String,
    ): ApiResponse<Nothing> {
        val memberId = this.getCurrentMemberId()
        this.expenseService.deleteExpense(memberId, expenseId)
        return ApiResponse.noContent()
    }

    private fun getCurrentMemberId(): String {
        return SecurityContextHolder.getContext().authentication.principal as String
    }
}
