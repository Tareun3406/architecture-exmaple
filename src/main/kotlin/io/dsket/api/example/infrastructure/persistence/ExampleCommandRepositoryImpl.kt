package io.dsket.api.example.infrastructure.persistence

import io.dsket.api.example.domain.model.Example
import io.dsket.api.example.domain.repository.ExampleCommandRepository
import io.dsket.api.example.infrastructure.persistence.jpa.ExampleJpaRepository
import io.dsket.api.example.infrastructure.persistence.mapper.ExampleInfraMapper
import org.springframework.stereotype.Repository

@Repository
class ExampleCommandRepositoryImpl(
    private val exampleJpaRepository: ExampleJpaRepository,
    private val exampleInfraMapper: ExampleInfraMapper,
): ExampleCommandRepository{
    override fun save(example: Example): Example {
        val exampleEntity = exampleJpaRepository.save(exampleInfraMapper.toEntity(example))
        return exampleInfraMapper.toDomain(exampleEntity)
    }

    override fun getReferenceById(id: Long): Example {
        val exampleEntity = exampleJpaRepository.getReferenceById(id)
        return exampleInfraMapper.toDomain(exampleEntity)
    }
}