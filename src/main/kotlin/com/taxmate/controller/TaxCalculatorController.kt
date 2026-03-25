package com.taxmate.controller

import com.taxmate.common.api.ApiResponse
import com.taxmate.dto.calculator.IncomeTaxRequest
import com.taxmate.dto.calculator.IncomeTaxResponse
import com.taxmate.dto.calculator.WithholdingTaxRequest
import com.taxmate.dto.calculator.WithholdingTaxResponse
import com.taxmate.service.TaxCalculatorService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/calculator")
class TaxCalculatorController(
    private val taxCalculatorService: TaxCalculatorService,
) {

    @PostMapping("/withholding")
    fun calculateWithholdingTax(
        @RequestBody @Valid request: WithholdingTaxRequest,
    ): ApiResponse<WithholdingTaxResponse> {
        val result = this.taxCalculatorService.calculateWithholdingTax(request)
        return ApiResponse.ok(result)
    }

    @PostMapping("/income-tax")
    fun calculateIncomeTax(
        @RequestBody @Valid request: IncomeTaxRequest,
    ): ApiResponse<IncomeTaxResponse> {
        val result = this.taxCalculatorService.calculateIncomeTax(request)
        return ApiResponse.ok(result)
    }
}
