package io.dsket.api.example.application.service.query.dto

data class ExampleSelectQueryResult( // fixme 조회(Query) 결과에 대한 DTO. Dto 접미사 사용
    val id: Long,
    val name: String
)