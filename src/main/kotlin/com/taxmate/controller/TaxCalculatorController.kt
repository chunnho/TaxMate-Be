package com.taxmate.controller

import com.taxmate.common.api.ApiResponse
import com.taxmate.dto.calculator.IncomeTaxRequest
import com.taxmate.dto.calculator.IncomeTaxResponse
import com.taxmate.dto.calculator.WithholdingTaxRequest
import com.taxmate.dto.calculator.WithholdingTaxResponse
import com.taxmate.service.TaxCalculatorService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "세금 계산기", description = "로그인 없이 사용 가능한 세금 계산 API")
@RestController
@RequestMapping("/api/calculator")
class TaxCalculatorController(
    private val taxCalculatorService: TaxCalculatorService,
) {

    @Operation(summary = "원천징수 계산", description = "수입 금액을 입력하면 3.3% 원천징수액과 실수령액을 계산합니다.")
    @PostMapping("/withholding")
    fun calculateWithholdingTax(
        @RequestBody @Valid request: WithholdingTaxRequest,
    ): ApiResponse<WithholdingTaxResponse> {
        val result = this.taxCalculatorService.calculateWithholdingTax(request)
        return ApiResponse.ok(result)
    }

    @Operation(summary = "종합소득세 간편 계산", description = "연간 수입과 경비를 입력하면 예상 종합소득세를 계산합니다. 경비를 입력하지 않으면 단순경비율(60%)이 적용됩니다.")
    @PostMapping("/income-tax")
    fun calculateIncomeTax(
        @RequestBody @Valid request: IncomeTaxRequest,
    ): ApiResponse<IncomeTaxResponse> {
        val result = this.taxCalculatorService.calculateIncomeTax(request)
        return ApiResponse.ok(result)
    }
}
