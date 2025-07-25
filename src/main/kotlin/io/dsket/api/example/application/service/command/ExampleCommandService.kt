package io.dsket.api.example.application.service.command

import io.dsket.api.example.application.service.command.dto.ExampleInsertCommand
import io.dsket.api.example.application.service.dto.ExampleResult
import io.dsket.api.example.application.service.command.dto.ExampleUpdateCommand
import io.dsket.api.example.application.service.command.mapper.ExampleCommandMapper
import io.dsket.api.example.domain.repository.ExampleCommandRepository
import org.springframework.stereotype.Service

@Service
class ExampleCommandService( // fixme 상세 비즈니스 로직. 도메인 객체를 다룸.
    private val exampleCommandRepository: ExampleCommandRepository,
    private val exampleCommandMapper: ExampleCommandMapper
) {

    fun insertExample(command: ExampleInsertCommand): ExampleResult { // fixme DB 수정에 대한 인자는 Command 접미사 사용
        val result = exampleCommandRepository.save(exampleCommandMapper.toDomain(command))
        return exampleCommandMapper.toResult(result) // fixme DB 수정에 대한 결과는 Result 접미사 사용
    }

    fun updateExample(command: ExampleUpdateCommand): ExampleResult {
        val example = exampleCommandRepository.getReferenceById(command.id)
        example.updateName(command.name) // fixme 도메인 수준의 비즈니스 로직은 해당 도메인객체가 책임을 가짐
        val result = exampleCommandRepository.save(example)
        return exampleCommandMapper.toResult(result)
    }
}