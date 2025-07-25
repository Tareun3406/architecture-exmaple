package io.dsket.api.example.application.service.query.mapper

import io.dsket.api.example.application.service.query.dto.ExampleContentQueryResult
import io.dsket.api.example.application.service.query.dto.ExampleSelectQueryResult
import io.dsket.api.example.application.service.query.projection.ExampleView
import io.dsket.api.example.domain.data.ExampleContentData
import org.springframework.stereotype.Component

@Component
class ExampleQueryMapper {
    fun toDto(view: ExampleView): ExampleSelectQueryResult {
        return ExampleSelectQueryResult(id = view.id, name = view.name)
    }
    fun toDto(data: ExampleContentData): ExampleContentQueryResult {
        return ExampleContentQueryResult(content = data.content)
    }
}