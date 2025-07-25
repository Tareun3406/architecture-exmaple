package io.dsket.api.common.infrastructure.external.notion

import io.dsket.api.common.infrastructure.external.notion.dto.NotionGetContentResponse
import org.springframework.stereotype.Component

@Component
class NotionClient {
    fun getContent(id: String): NotionGetContentResponse {
        return NotionGetContentResponse("idValue", "contentValue")
    }
}