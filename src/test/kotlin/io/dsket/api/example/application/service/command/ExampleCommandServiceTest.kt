package io.dsket.api.example.application.service.command

import io.dsket.api.example.application.service.command.dto.ExampleInsertCommand
import io.dsket.api.example.application.service.command.dto.ExampleUpdateCommand
import io.dsket.api.example.application.service.command.mapper.ExampleCommandMapper
import io.dsket.api.example.application.service.dto.ExampleResult
import io.dsket.api.example.domain.model.Example
import io.dsket.api.example.domain.repository.ExampleCommandRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.never

@ExtendWith(MockitoExtension::class)
@DisplayName("ExampleCommandService 단위 테스트")
class ExampleCommandServiceTest {

    @Mock
    private lateinit var exampleCommandRepository: ExampleCommandRepository

    @Mock
    private lateinit var exampleCommandMapper: ExampleCommandMapper

    @InjectMocks
    private lateinit var exampleCommandService: ExampleCommandService

    private lateinit var sampleExample: Example
    private lateinit var sampleInsertCommand: ExampleInsertCommand
    private lateinit var sampleUpdateCommand: ExampleUpdateCommand
    private lateinit var sampleResult: ExampleResult

    @BeforeEach
    fun setUp() {
        sampleExample = Example(id = 1L, name = "Test Example")
        sampleInsertCommand = ExampleInsertCommand(id = 1L, name = "Test Example")
        sampleUpdateCommand = ExampleUpdateCommand(id = 1L, name = "Updated Example")
        sampleResult = ExampleResult(id = 1L, name = "Test Example")
    }

    @Test
    @DisplayName("Example 삽입 - 성공")
    fun `should insert example successfully`() {
        // given
        `when`(exampleCommandMapper.toDomain(sampleInsertCommand)).thenReturn(sampleExample)
        `when`(exampleCommandRepository.save(sampleExample)).thenReturn(sampleExample)
        `when`(exampleCommandMapper.toResult(sampleExample)).thenReturn(sampleResult)

        // when
        val result = exampleCommandService.insertExample(sampleInsertCommand)

        // then
        assertNotNull(result)
        assertEquals(sampleResult.id, result.id)
        assertEquals(sampleResult.name, result.name)

        // 의존성 호출 검증
        verify(exampleCommandMapper).toDomain(sampleInsertCommand)
        verify(exampleCommandRepository).save(sampleExample)
        verify(exampleCommandMapper).toResult(sampleExample)
    }

    @Test
    @DisplayName("Example 삽입 - 매퍼 호출 순서 검증")
    fun `should call mapper and repository in correct order for insert`() {
        // given
        `when`(exampleCommandMapper.toDomain(any())).thenReturn(sampleExample)
        `when`(exampleCommandRepository.save(any())).thenReturn(sampleExample)
        `when`(exampleCommandMapper.toResult(any())).thenReturn(sampleResult)

        // when
        exampleCommandService.insertExample(sampleInsertCommand)

        // then
        val inOrder = inOrder(exampleCommandMapper, exampleCommandRepository)
        inOrder.verify(exampleCommandMapper).toDomain(sampleInsertCommand)
        inOrder.verify(exampleCommandRepository).save(sampleExample)
        inOrder.verify(exampleCommandMapper).toResult(sampleExample)
    }

    @Test
    @DisplayName("Example 업데이트 - 성공")
    fun `should update example successfully`() {
        // given
        val existingExample = Example(id = 1L, name = "Original Name")
        val updatedExample = Example(id = 1L, name = "Updated Example")
        val expectedResult = ExampleResult(id = 1L, name = "Updated Example")

        `when`(exampleCommandRepository.getReferenceById(1L)).thenReturn(existingExample)
        `when`(exampleCommandRepository.save(existingExample)).thenReturn(updatedExample)
        `when`(exampleCommandMapper.toResult(updatedExample)).thenReturn(expectedResult)

        // when
        val result = exampleCommandService.updateExample(sampleUpdateCommand)

        // then
        assertNotNull(result)
        assertEquals(expectedResult.id, result.id)
        assertEquals(expectedResult.name, result.name)

        // 도메인 로직 호출 확인 (updateName이 호출되었는지는 도메인 객체 상태로 확인)
        assertEquals("Updated Example", existingExample.name)

        // 의존성 호출 검증
        verify(exampleCommandRepository).getReferenceById(1L)
        verify(exampleCommandRepository).save(existingExample)
        verify(exampleCommandMapper).toResult(updatedExample)
    }

    @Test
    @DisplayName("Example 업데이트 - 도메인 로직 호출 검증")
    fun `should call domain logic during update`() {
        // given
        val existingExample = spy(Example(id = 1L, name = "Original Name"))
        val updatedExample = Example(id = 1L, name = "Updated Example")

        `when`(exampleCommandRepository.getReferenceById(1L)).thenReturn(existingExample)
        `when`(exampleCommandRepository.save(existingExample)).thenReturn(updatedExample)
        `when`(exampleCommandMapper.toResult(updatedExample)).thenReturn(sampleResult)

        // when
        exampleCommandService.updateExample(sampleUpdateCommand)

        // then
        // 도메인 객체의 updateName 메서드가 호출되었는지 확인
        verify(existingExample).updateName("Updated Example")
    }

    @Test
    @DisplayName("Example 업데이트 - 호출 순서 검증")
    fun `should call methods in correct order for update`() {
        // given
        val existingExample = Example(id = 1L, name = "Original Name")
        
        `when`(exampleCommandRepository.getReferenceById(1L)).thenReturn(existingExample)
        `when`(exampleCommandRepository.save(existingExample)).thenReturn(existingExample)
        `when`(exampleCommandMapper.toResult(existingExample)).thenReturn(sampleResult)

        // when
        exampleCommandService.updateExample(sampleUpdateCommand)

        // then
        val inOrder = inOrder(exampleCommandRepository, exampleCommandMapper)
        inOrder.verify(exampleCommandRepository).getReferenceById(1L)
        inOrder.verify(exampleCommandRepository).save(existingExample)
        inOrder.verify(exampleCommandMapper).toResult(existingExample)
    }

    @Test
    @DisplayName("Example 업데이트 - Repository에서 예외 발생 시 전파")
    fun `should propagate exception when repository throws exception during update`() {
        // given
        `when`(exampleCommandRepository.getReferenceById(1L))
            .thenThrow(RuntimeException("Entity not found"))

        // when & then
        assertThrows(RuntimeException::class.java) {
            exampleCommandService.updateExample(sampleUpdateCommand)
        }

        // save나 mapper는 호출되지 않아야 함
        verify(exampleCommandRepository, never()).save(any())
        verify(exampleCommandMapper, never()).toResult(any())
    }

    @Test
    @DisplayName("Example 삽입 - Repository에서 예외 발생 시 전파")
    fun `should propagate exception when repository throws exception during insert`() {
        // given
        `when`(exampleCommandMapper.toDomain(sampleInsertCommand)).thenReturn(sampleExample)
        `when`(exampleCommandRepository.save(sampleExample))
            .thenThrow(RuntimeException("Database error"))

        // when & then
        assertThrows(RuntimeException::class.java) {
            exampleCommandService.insertExample(sampleInsertCommand)
        }

        // toResult는 호출되지 않아야 함
        verify(exampleCommandMapper, never()).toResult(any())
    }
}
