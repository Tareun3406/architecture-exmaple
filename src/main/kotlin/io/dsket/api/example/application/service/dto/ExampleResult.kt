package io.dsket.api.example.application.service.dto

data class ExampleResult( // fixme 작업 수행에 대한 결과를 리턴하기 위한 DTO. Result 접미사 사용
    val id: Long,
    val name: String
)