package io.dsket.api.common.`interface`.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val httpStatus: HttpStatus,
    val message: String
) {

    // ExampleDomain
    ExampleDomain_0001(HttpStatus.BAD_REQUEST, "name is empty"),
}