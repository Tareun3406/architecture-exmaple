package io.dsket.api.common.`interface`.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(baseException: BaseException): ResponseEntity<ExceptionResponse> {
        val body = ExceptionResponse(baseException.errorCode.message)
        return ResponseEntity(body, baseException.errorCode.httpStatus)
    }
}