package io.dsket.api.example.domain.repository

import io.dsket.api.example.application.service.query.projection.ExampleView
import io.dsket.api.example.domain.model.Example

interface ExampleRepository {
    fun save(example: Example): Example
    fun getReferenceById(id: Long): Example
    fun getViewById(id: Long): ExampleView
}