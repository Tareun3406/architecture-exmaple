package io.dsket.api.example.infrastructure.external.mapper

import io.dsket.api.common.infrastructure.external.notion.dto.NotionGetContentResponse
import io.dsket.api.example.domain.data.ExampleContentData
import org.springframework.stereotype.Component

@Component
class ExampleGatewayMapper {
    fun toData(reponse: NotionGetContentResponse): ExampleContentData {
        return ExampleContentData(reponse.content)
    }
}