package com.taxmate.controller

import com.taxmate.common.api.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "헬스체크", description = "서버 상태 확인")
@RestController
class HealthController {

    @Operation(summary = "서버 상태 확인")
    @GetMapping("/api/health")
    fun health(): ApiResponse<Map<String, String>> {
        return ApiResponse.ok(mapOf("status" to "ok"))
    }
}
