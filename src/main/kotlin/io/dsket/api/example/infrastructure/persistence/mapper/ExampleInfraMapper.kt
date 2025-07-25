package io.dsket.api.example.infrastructure.persistence.mapper

import io.dsket.api.example.domain.model.Example
import io.dsket.api.example.infrastructure.persistence.jpa.entity.ExampleEntity
import org.springframework.stereotype.Component

@Component
class ExampleInfraMapper {
    fun toEntity(example: Example): ExampleEntity {
        return ExampleEntity(id = example.id, name = example.name)
    }

    fun toDomain(exampleEntity: ExampleEntity): Example {
        return Example(id = exampleEntity.id, name = exampleEntity.name)
    }
}