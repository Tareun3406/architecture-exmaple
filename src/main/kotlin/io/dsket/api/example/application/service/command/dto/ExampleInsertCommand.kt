package io.dsket.api.example.application.service.command.dto

data class ExampleInsertCommand( // fixme 데이터를 변경하기 위한 DTO. Command 접미사 사용
    val id: Long,
    val name: String
)