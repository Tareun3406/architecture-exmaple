package io.dsket.api.example.application.facade

import io.dsket.api.example.application.facade.dto.ExampleContentOutput
import io.dsket.api.example.application.facade.dto.ExampleInsertInput
import io.dsket.api.example.application.facade.dto.ExampleOutput
import io.dsket.api.example.application.facade.dto.ExampleUpdateInput
import io.dsket.api.example.application.facade.mapper.ExampleFacadeMapper
import io.dsket.api.example.application.service.command.ExampleCommandService
import io.dsket.api.example.application.service.query.ExampleQueryService
import org.springframework.stereotype.Service

@Service
class ExampleFacade( // fixme 유즈 케이스 별 서비스 로직의 흐름 제어. 다른 패키지의 서비스 호출 가능. Facade 패턴
    private val exampleCommandService: ExampleCommandService,
    private val exampleQueryService: ExampleQueryService,
    private val exampleFacadeMapper: ExampleFacadeMapper,
) {
    fun getExample(id: Long): ExampleOutput {
        val exampleDto = exampleQueryService.selectExample(id)
        return exampleFacadeMapper.toOutput(exampleDto)
    }

    fun insertExample(input: ExampleInsertInput): ExampleOutput {
        val command = exampleFacadeMapper.toCommand(input)
        val exampleResult = exampleCommandService.insertExample(command)
        return exampleFacadeMapper.toOutput(exampleResult)
    }

    fun updateExample(input: ExampleUpdateInput): ExampleOutput {
        val command = exampleFacadeMapper.toCommand(input)
        val exampleResult = exampleCommandService.updateExample(command)
        return exampleFacadeMapper.toOutput(exampleResult)
    }

    fun getContents(id: String): ExampleContentOutput {
        return exampleFacadeMapper.toOutput(exampleQueryService.getContents(id))
    }
}