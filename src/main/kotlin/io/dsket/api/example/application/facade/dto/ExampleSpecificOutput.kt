package io.dsket.api.example.application.facade.dto

data class ExampleSpecificOutput( // fixme 특정 메서드에 특화된 DTO 예시.(추가 필드), 다른 계층 공통(접미사는 해당 계층의 규칙을 따름)
    val example: ExampleOutput,
    val specific: Any
)