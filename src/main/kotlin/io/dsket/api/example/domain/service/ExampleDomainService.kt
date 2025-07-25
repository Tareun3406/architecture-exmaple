package io.dsket.api.example.domain.service

import io.dsket.api.example.domain.model.Example
import org.springframework.stereotype.Service

@Service
class ExampleDomainService { // fixme DDD 에서 Entity(도메인 객체)들을 조합하고 만들기 위한 서비스
    fun createExample(): Example {
        return Example(0, "name")
    }
}