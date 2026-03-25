package com.taxmate.controller

import com.taxmate.common.api.ApiResponse
import com.taxmate.dto.income.CreateIncomeRequest
import com.taxmate.dto.income.IncomeResponse
import com.taxmate.dto.income.UpdateIncomeRequest
import com.taxmate.service.IncomeService
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

@Tag(name = "수입 관리", description = "수입 등록, 조회, 수정, 삭제 (로그인 필요)")
@RestController
@RequestMapping("/api/incomes")
class IncomeController(
    private val incomeService: IncomeService,
) {

    @Operation(summary = "수입 등록", description = "새로운 수입 내역을 등록합니다. 3.3% 원천징수가 자동 계산됩니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createIncome(
        @RequestBody @Valid request: CreateIncomeRequest,
    ): ApiResponse<IncomeResponse> {
        val memberId = this.getCurrentMemberId()
        val result = this.incomeService.createIncome(memberId, request)
        return ApiResponse.created(result)
    }

    @Operation(summary = "수입 목록 조회", description = "내 수입 목록을 조회합니다. year, month 파라미터로 월별 필터링이 가능합니다.")
    @GetMapping
    fun getIncomes(
        @RequestParam(required = false) year: Int?,
        @RequestParam(required = false) month: Int?,
    ): ApiResponse<List<IncomeResponse>> {
        val memberId = this.getCurrentMemberId()
        val result = this.incomeService.getIncomes(memberId, year, month)
        return ApiResponse.ok(result)
    }

    @Operation(summary = "수입 상세 조회", description = "수입 내역 1건을 조회합니다.")
    @GetMapping("/{incomeId}")
    fun getIncome(
        @PathVariable incomeId: String,
    ): ApiResponse<IncomeResponse> {
        val memberId = this.getCurrentMemberId()
        val result = this.incomeService.getIncome(memberId, incomeId)
        return ApiResponse.ok(result)
    }

    @Operation(summary = "수입 수정", description = "수입 내역을 수정합니다.")
    @PutMapping("/{incomeId}")
    fun updateIncome(
        @PathVariable incomeId: String,
        @RequestBody @Valid request: UpdateIncomeRequest,
    ): ApiResponse<IncomeResponse> {
        val memberId = this.getCurrentMemberId()
        val result = this.incomeService.updateIncome(memberId, incomeId, request)
        return ApiResponse.ok(result)
    }

    @Operation(summary = "수입 삭제", description = "수입 내역을 삭제합니다. (소프트 삭제)")
    @DeleteMapping("/{incomeId}")
    fun deleteIncome(
        @PathVariable incomeId: String,
    ): ApiResponse<Nothing> {
        val memberId = this.getCurrentMemberId()
        this.incomeService.deleteIncome(memberId, incomeId)
        return ApiResponse.noContent()
    }

    private fun getCurrentMemberId(): String {
        return SecurityContextHolder.getContext().authentication.principal as String
    }
}
