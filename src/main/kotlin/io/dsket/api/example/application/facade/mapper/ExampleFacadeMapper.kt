package io.dsket.api.example.application.facade.mapper

import io.dsket.api.example.application.facade.dto.ExampleContentOutput
import io.dsket.api.example.application.facade.dto.ExampleInsertInput
import io.dsket.api.example.application.facade.dto.ExampleOutput
import io.dsket.api.example.application.facade.dto.ExampleUpdateInput
import io.dsket.api.example.application.service.command.dto.ExampleInsertCommand
import io.dsket.api.example.application.service.dto.ExampleResult
import io.dsket.api.example.application.service.command.dto.ExampleUpdateCommand
import io.dsket.api.example.application.service.query.dto.ExampleContentQueryResult
import io.dsket.api.example.application.service.query.dto.ExampleSelectQueryResult
import org.springframework.stereotype.Component

@Component
class ExampleFacadeMapper { // todo  mapstruct 사용 고려
    fun toOutput(example: ExampleSelectQueryResult): ExampleOutput {
        return ExampleOutput(id = example.id, name = example.name)
    }

    fun toOutput(result: ExampleResult): ExampleOutput {
        return ExampleOutput(id = result.id, name = result.name)
    }

    fun toCommand(input: ExampleInsertInput): ExampleInsertCommand {
        return ExampleInsertCommand(id = input.id, name = input.name)
    }

    fun toCommand(input: ExampleUpdateInput): ExampleUpdateCommand {
        return ExampleUpdateCommand(id = input.id, name = input.name)
    }

    fun toOutput(dto: ExampleContentQueryResult): ExampleContentOutput {
        return ExampleContentOutput(content = dto.content)
    }
}