package io.dsket.api.example.`interface`.web.controller

import io.dsket.api.example.application.facade.ExampleFacade
import io.dsket.api.example.`interface`.web.dto.ExampleInsertRequest
import io.dsket.api.example.`interface`.web.dto.ExampleResponse
import io.dsket.api.example.`interface`.web.dto.ExampleUpdateRequest
import io.dsket.api.example.`interface`.web.mapper.ExampleWebMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/example")
class ExampleController( // fixme 외부 소통에 대한 책임(Interface), 비즈니스 로직 진입점(facade) 호출
    private val exampleFacade: ExampleFacade,
    private val exampleWebMapper: ExampleWebMapper
) {

    @GetMapping("/{id}")
    fun getExample(@PathVariable("id") id: Long): ExampleResponse {
        val output = exampleFacade.getExample(id)
        return exampleWebMapper.toResponse(output)
    }

    @PostMapping
    fun insertExample(@RequestBody request: ExampleInsertRequest): ExampleResponse {
        val input = exampleWebMapper.toInput(request) // fixme 약속된 요청 포멧을 내부 로직에 전달하기 위한 객체로 변경.
        val output = exampleFacade.insertExample(input)
        return exampleWebMapper.toResponse(output) // fixme 내부 로직에서 반환된 출력을 약속된 응답 포멧에 맞추어 변환
    }

    @PostMapping("/{id}")
    fun updateExample(@PathVariable("id") id: Long, @RequestBody request: ExampleUpdateRequest): ExampleResponse {
        val input = exampleWebMapper.toInput(id, request)
        val output = exampleFacade.updateExample(input)
        return exampleWebMapper.toResponse(output)
    }
}