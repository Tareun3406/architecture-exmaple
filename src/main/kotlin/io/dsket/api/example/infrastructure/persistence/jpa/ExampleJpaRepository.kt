package io.dsket.api.example.infrastructure.persistence.jpa

import io.dsket.api.example.application.service.query.projection.ExampleView
import io.dsket.api.example.infrastructure.persistence.jpa.entity.ExampleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ExampleJpaRepository : JpaRepository<ExampleEntity, Long> {
    fun getViewById(id: Long): ExampleView
}