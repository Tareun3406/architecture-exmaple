package io.dsket.api.example.`interface`.web.mapper

import io.dsket.api.example.application.facade.dto.ExampleInsertInput
import io.dsket.api.example.application.facade.dto.ExampleOutput
import io.dsket.api.example.application.facade.dto.ExampleUpdateInput
import io.dsket.api.example.`interface`.web.dto.ExampleInsertRequest
import io.dsket.api.example.`interface`.web.dto.ExampleResponse
import io.dsket.api.example.`interface`.web.dto.ExampleUpdateRequest
import org.springframework.stereotype.Component

@Component
class ExampleWebMapper { // todo  mapstruct 사용 고려
    fun toResponse(output: ExampleOutput): ExampleResponse {
        return ExampleResponse(id = output.id, name = output.name)
    }
    fun toInput(request: ExampleInsertRequest): ExampleInsertInput {
        return ExampleInsertInput(id = request.id, name = request.name)
    }
    fun toInput(requestId: Long, request: ExampleUpdateRequest): ExampleUpdateInput {
        return ExampleUpdateInput(id = requestId, name = request.name)
    }
}