package io.dsket.api.example.domain.repository

import io.dsket.api.example.application.service.query.projection.ExampleView

interface ExampleQueryRepository {
    fun getViewById(id: Long): ExampleView
}