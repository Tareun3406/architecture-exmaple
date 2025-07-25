package io.dsket.api.example.`interface`.web.dto

data class ExampleResponse( // fixme 응답 바디의 명세(format)를 객체로 표현. Response 접미사 사용
    val id: Long,
    val name: String
)
