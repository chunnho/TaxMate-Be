package com.taxmate.common.exception

class ApiException(
    val errorCode: ErrorCode,
) : RuntimeException(errorCode.message)
