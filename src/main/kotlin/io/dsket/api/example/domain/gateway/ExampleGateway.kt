package io.dsket.api.example.domain.gateway

import io.dsket.api.example.domain.data.ExampleContentData

interface ExampleGateway {
    fun getContent(id: String): ExampleContentData
}