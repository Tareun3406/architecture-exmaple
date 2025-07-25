package io.dsket.api.common.`interface`.exception

class BaseException(
    val errorCode: ErrorCode,
    cause: Throwable? = null,
): RuntimeException(cause) {
}