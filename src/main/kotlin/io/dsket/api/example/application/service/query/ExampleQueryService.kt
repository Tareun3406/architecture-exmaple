package io.dsket.api.example.application.service.query

import io.dsket.api.example.application.service.query.dto.ExampleSelectQueryResult
import io.dsket.api.example.application.service.query.dto.ExampleContentQueryResult
import io.dsket.api.example.application.service.query.mapper.ExampleQueryMapper
import io.dsket.api.example.domain.gateway.ExampleGateway
import io.dsket.api.example.domain.repository.ExampleQueryRepository
import org.springframework.stereotype.Service

@Service
class ExampleQueryService(
    private val exampleGateway: ExampleGateway,
    private val exampleQueryRepository: ExampleQueryRepository,
    private val exampleQueryMapper: ExampleQueryMapper
) {
    fun selectExample(id: Long): ExampleSelectQueryResult { // fixme 복잡한 인자를 요구하는 조회일경우 ExampleQuery 객체를 인자로 받음
        val result = exampleQueryRepository.getViewById(id)
        return exampleQueryMapper.toDto(result) // fixme 조회에 대한 결과는 Dto 접미사 사용
    }

    fun getContents(id: String): ExampleContentQueryResult {
        val result = exampleGateway.getContent(id) // fixme 복잡한 인자를 요구하는 외부 요청일 경우 Param 객체를 인자로 전달
        return exampleQueryMapper.toDto(result)
    }
}