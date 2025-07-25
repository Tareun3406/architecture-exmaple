package io.dsket.api.example.domain.repository

import io.dsket.api.example.domain.model.Example

interface ExampleCommandRepository {
    fun save(example: Example): Example
    fun getReferenceById(id: Long): Example
}