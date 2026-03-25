package com.taxmate.controller

import com.taxmate.common.api.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

    @GetMapping("/api/health")
    fun health(): ApiResponse<Map<String, String>> {
        return ApiResponse.ok(mapOf("status" to "ok"))
    }
}
