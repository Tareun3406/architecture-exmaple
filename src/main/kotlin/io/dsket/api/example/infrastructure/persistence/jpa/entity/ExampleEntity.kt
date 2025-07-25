package io.dsket.api.example.infrastructure.persistence.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class ExampleEntity(
    @Id
    val id: Long = 0L,

    @Column
    val name: String
)