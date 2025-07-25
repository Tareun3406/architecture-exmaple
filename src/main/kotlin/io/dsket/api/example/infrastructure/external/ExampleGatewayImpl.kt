package io.dsket.api.example.infrastructure.external

import io.dsket.api.common.infrastructure.external.notion.NotionClient
import io.dsket.api.example.domain.data.ExampleContentData
import io.dsket.api.example.domain.gateway.ExampleGateway
import io.dsket.api.example.infrastructure.external.mapper.ExampleGatewayMapper
import org.springframework.stereotype.Component

@Component
class ExampleGatewayImpl(
    private val notionClient: NotionClient,
    private val exampleGatewayMapper: ExampleGatewayMapper
): ExampleGateway {
    override fun getContent(id: String): ExampleContentData {
        val content = notionClient.getContent(id)
        return exampleGatewayMapper.toData(content)
    }
}