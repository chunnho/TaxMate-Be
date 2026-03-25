package com.taxmate.controller

import com.taxmate.common.api.ApiResponse
import com.taxmate.dto.dashboard.MonthlySummaryResponse
import com.taxmate.dto.dashboard.YearlySummaryResponse
import com.taxmate.service.DashboardService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "대시보드", description = "월별/연간 수입/지출/세금 요약 (로그인 필요)")
@RestController
@RequestMapping("/api/dashboard")
class DashboardController(
    private val dashboardService: DashboardService,
) {

    @Operation(
        summary = "월별 요약",
        description = "해당 월의 총 수입, 총 지출, 원천징수액, 순수익을 조회합니다.",
    )
    @GetMapping("/monthly")
    fun getMonthlySummary(
        @RequestParam year: Int,
        @RequestParam month: Int,
    ): ApiResponse<MonthlySummaryResponse> {
        val memberId = this.getCurrentMemberId()
        val result = this.dashboardService.getMonthlySummary(memberId, year, month)
        return ApiResponse.ok(result)
    }

    @Operation(
        summary = "연간 요약",
        description = "해당 연도의 총 수입, 총 지출, 예상 종소세, 환급/추가납부 예상액을 조회합니다. 월별 브레이크다운 포함.",
    )
    @GetMapping("/yearly")
    fun getYearlySummary(
        @RequestParam year: Int,
    ): ApiResponse<YearlySummaryResponse> {
        val memberId = this.getCurrentMemberId()
        val result = this.dashboardService.getYearlySummary(memberId, year)
        return ApiResponse.ok(result)
    }

    private fun getCurrentMemberId(): String {
        return SecurityContextHolder.getContext().authentication.principal as String
    }
}
