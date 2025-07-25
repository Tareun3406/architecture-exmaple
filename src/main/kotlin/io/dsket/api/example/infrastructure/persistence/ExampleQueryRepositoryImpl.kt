package io.dsket.api.example.infrastructure.persistence

import io.dsket.api.example.application.service.query.projection.ExampleView
import io.dsket.api.example.domain.repository.ExampleQueryRepository
import io.dsket.api.example.infrastructure.persistence.jpa.ExampleJpaRepository
import org.springframework.stereotype.Repository

@Repository
class ExampleQueryRepositoryImpl(
    private val exampleJpaRepository: ExampleJpaRepository,
): ExampleQueryRepository {
    override fun getViewById(id: Long): ExampleView {
        return exampleJpaRepository.getViewById(id)
    }
}