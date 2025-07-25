package io.dsket.api.example.domain.service

import io.dsket.api.example.domain.model.Example
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("ExampleDomainService 테스트")
class ExampleDomainServiceTest {

    private lateinit var exampleDomainService: ExampleDomainService

    @BeforeEach
    fun setUp() {
        exampleDomainService = ExampleDomainService()
    }

    @Test
    @DisplayName("Example 생성 - 성공")
    fun `should create Example successfully`() {
        // when
        val result = exampleDomainService.createExample()

        // then
        assertNotNull(result)
        assertInstanceOf(Example::class.java, result)
        assertEquals(0L, result.id)
        assertEquals("name", result.name)
    }

    @Test
    @DisplayName("Example 생성 - 여러 번 호출 시 독립적인 객체 생성")
    fun `should create independent Example instances`() {
        // when
        val example1 = exampleDomainService.createExample()
        val example2 = exampleDomainService.createExample()

        // then
        assertNotSame(example1, example2) // 서로 다른 객체 인스턴스
        assertEquals(example1.id, example2.id) // 같은 기본값
        assertEquals(example1.name, example2.name) // 같은 기본값
        
        // 하나를 수정해도 다른 것에 영향 없음
        example1.updateName("Modified Name")
        assertEquals("Modified Name", example1.name)
        assertEquals("name", example2.name) // 원본 유지
    }

    @Test
    @DisplayName("생성된 Example 객체의 도메인 로직 동작 확인")
    fun `should verify domain logic works on created Example`() {
        // given
        val example = exampleDomainService.createExample()

        // when
        example.updateName("Updated Name")

        // then
        assertEquals("Updated Name", example.name)
        assertEquals(0L, example.id) // ID는 변경되지 않음
    }
}
