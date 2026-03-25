package com.taxmate.common.api

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T? = null,
) {
    companion object {
        fun <T> ok(data: T): ApiResponse<T> {
            return ApiResponse(
                status = HttpStatus.OK.value(),
                message = "success",
                data = data,
            )
        }

        fun <T> created(data: T): ApiResponse<T> {
            return ApiResponse(
                status = HttpStatus.CREATED.value(),
                message = "created",
                data = data,
            )
        }

        fun noContent(): ApiResponse<Nothing> {
            return ApiResponse(
                status = HttpStatus.NO_CONTENT.value(),
                message = "no content",
            )
        }

        fun error(status: HttpStatus, message: String): ApiResponse<Nothing> {
            return ApiResponse(
                status = status.value(),
                message = message,
            )
        }
    }
}
