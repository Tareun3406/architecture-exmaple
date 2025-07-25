package io.dsket.api.example.application.service.command.mapper

import io.dsket.api.example.application.service.command.dto.ExampleInsertCommand
import io.dsket.api.example.application.service.query.dto.ExampleSelectQueryResult
import io.dsket.api.example.application.service.dto.ExampleResult
import io.dsket.api.example.domain.model.Example
import org.springframework.stereotype.Component

@Component
class ExampleCommandMapper { // todo mapstruct 사용 고려
    fun toDto(example: Example): ExampleSelectQueryResult {
        return ExampleSelectQueryResult(id = example.id, name = example.name)
    }
    fun toResult(example: Example): ExampleResult {
        return ExampleResult(id = example.id, name = example.name)
    }
    fun toDomain(command: ExampleInsertCommand): Example {
        return Example(id = command.id, name = command.name)
    }
}