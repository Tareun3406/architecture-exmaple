package io.dsket.api.example.application.service.query

import io.dsket.api.example.application.service.query.dto.ExampleContentQueryResult
import io.dsket.api.example.application.service.query.dto.ExampleSelectQueryResult
import io.dsket.api.example.application.service.query.mapper.ExampleQueryMapper
import io.dsket.api.example.application.service.query.projection.ExampleView
import io.dsket.api.example.domain.data.ExampleContentData
import io.dsket.api.example.domain.gateway.ExampleGateway
import io.dsket.api.example.domain.repository.ExampleQueryRepository
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
@DisplayName("ExampleQueryService 단위 테스트")
class ExampleQueryServiceTest {

    @Mock
    private lateinit var exampleGateway: ExampleGateway

    @Mock
    private lateinit var exampleQueryRepository: ExampleQueryRepository

    @Mock
    private lateinit var exampleQueryMapper: ExampleQueryMapper

    @InjectMocks
    private lateinit var exampleQueryService: ExampleQueryService

    private lateinit var sampleView: ExampleView
    private lateinit var sampleDto: ExampleSelectQueryResult
    private lateinit var sampleContentData: ExampleContentData
    private lateinit var sampleContentDto: ExampleContentQueryResult

    @BeforeEach
    fun setUp() {
        sampleView = ExampleView(id = 1L, name = "Test Example")
        sampleDto = ExampleSelectQueryResult(id = 1L, name = "Test Example")
        sampleContentData = ExampleContentData(content = "Sample Content")
        sampleContentDto = ExampleContentQueryResult(content = "Sample Content")
    }

    @Test
    @DisplayName("Example 조회 - 성공")
    fun `should select example successfully`() {
        // given
        val exampleId = 1L
        `when`(exampleQueryRepository.getViewById(exampleId)).thenReturn(sampleView)
        `when`(exampleQueryMapper.toDto(sampleView)).thenReturn(sampleDto)

        // when
        val result = exampleQueryService.selectExample(exampleId)

        // then
        assertNotNull(result)
        assertEquals(sampleDto.id, result.id)
        assertEquals(sampleDto.name, result.name)

        // 의존성 호출 검증
        verify(exampleQueryRepository).getViewById(exampleId)
        verify(exampleQueryMapper).toDto(sampleView)
    }

    @Test
    @DisplayName("Example 조회 - 호출 순서 검증")
    fun `should call repository and mapper in correct order for select`() {
        // given
        val exampleId = 1L
        `when`(exampleQueryRepository.getViewById(exampleId)).thenReturn(sampleView)
        `when`(exampleQueryMapper.toDto(sampleView)).thenReturn(sampleDto)

        // when
        exampleQueryService.selectExample(exampleId)

        // then
        val inOrder = inOrder(exampleQueryRepository, exampleQueryMapper)
        inOrder.verify(exampleQueryRepository).getViewById(exampleId)
        inOrder.verify(exampleQueryMapper).toDto(sampleView)
    }

    @Test
    @DisplayName("Example 조회 - Repository에서 예외 발생 시 전파")
    fun `should propagate exception when repository throws exception during select`() {
        // given
        val exampleId = 1L
        `when`(exampleQueryRepository.getViewById(exampleId))
            .thenThrow(RuntimeException("Entity not found"))

        // when & then
        assertThrows(RuntimeException::class.java) {
            exampleQueryService.selectExample(exampleId)
        }

        // mapper는 호출되지 않아야 함
        verify(exampleQueryMapper, never()).toDto(any<ExampleView>())
    }

    @Test
    @DisplayName("외부 컨텐츠 조회 - 성공")
    fun `should get contents successfully`() {
        // given
        val contentId = "content-123"
        `when`(exampleGateway.getContent(contentId)).thenReturn(sampleContentData)
        `when`(exampleQueryMapper.toDto(sampleContentData)).thenReturn(sampleContentDto)

        // when
        val result = exampleQueryService.getContents(contentId)

        // then
        assertNotNull(result)
        assertEquals(sampleContentDto.content, result.content)

        // 의존성 호출 검증
        verify(exampleGateway).getContent(contentId)
        verify(exampleQueryMapper).toDto(sampleContentData)
    }

    @Test
    @DisplayName("외부 컨텐츠 조회 - 호출 순서 검증")
    fun `should call gateway and mapper in correct order for getContents`() {
        // given
        val contentId = "content-123"
        `when`(exampleGateway.getContent(contentId)).thenReturn(sampleContentData)
        `when`(exampleQueryMapper.toDto(sampleContentData)).thenReturn(sampleContentDto)

        // when
        exampleQueryService.getContents(contentId)

        // then
        val inOrder = inOrder(exampleGateway, exampleQueryMapper)
        inOrder.verify(exampleGateway).getContent(contentId)
        inOrder.verify(exampleQueryMapper).toDto(sampleContentData)
    }

    @Test
    @DisplayName("외부 컨텐츠 조회 - Gateway에서 예외 발생 시 전파")
    fun `should propagate exception when gateway throws exception during getContents`() {
        // given
        val contentId = "content-123"
        `when`(exampleGateway.getContent(contentId))
            .thenThrow(RuntimeException("External API error"))

        // when & then
        assertThrows(RuntimeException::class.java) {
            exampleQueryService.getContents(contentId)
        }

        // mapper는 호출되지 않아야 함
        verify(exampleQueryMapper, never()).toDto(any<ExampleContentData>())
    }

    @Test
    @DisplayName("다양한 ID로 Example 조회 테스트")
    fun `should handle different example ids`() {
        // given
        val testIds = listOf(1L, 100L, 999L)
        
        testIds.forEach { id ->
            val view = ExampleView(id = id, name = "Example $id")
            val dto = ExampleSelectQueryResult(id = id, name = "Example $id")
            
            `when`(exampleQueryRepository.getViewById(id)).thenReturn(view)
            `when`(exampleQueryMapper.toDto(view)).thenReturn(dto)
        }

        // when & then
        testIds.forEach { id ->
            val result = exampleQueryService.selectExample(id)
            assertEquals(id, result.id)
            assertEquals("Example $id", result.name)
        }

        // 모든 ID에 대해 호출되었는지 검증
        testIds.forEach { id ->
            verify(exampleQueryRepository).getViewById(id)
        }
    }

    @Test
    @DisplayName("다양한 컨텐츠 ID로 외부 컨텐츠 조회 테스트")
    fun `should handle different content ids`() {
        // given
        val testIds = listOf("content-1", "content-abc", "content-xyz")
        
        testIds.forEach { id ->
            val data = ExampleContentData(content = "Content for $id")
            val dto = ExampleContentQueryResult(content = "Content for $id")
            
            `when`(exampleGateway.getContent(id)).thenReturn(data)
            `when`(exampleQueryMapper.toDto(data)).thenReturn(dto)
        }

        // when & then
        testIds.forEach { id ->
            val result = exampleQueryService.getContents(id)
            assertEquals("Content for $id", result.content)
        }

        // 모든 ID에 대해 호출되었는지 검증
        testIds.forEach { id ->
            verify(exampleGateway).getContent(id)
        }
    }

    @Test
    @DisplayName("Mapper 호출 횟수 검증")
    fun `should verify mapper call count`() {
        // given
        `when`(exampleQueryRepository.getViewById(1L)).thenReturn(sampleView)
        `when`(exampleQueryRepository.getViewById(2L)).thenReturn(ExampleView(2L, "Example 2"))
        `when`(exampleQueryMapper.toDto(any<ExampleView>())).thenReturn(sampleDto)
        `when`(exampleGateway.getContent("content-1")).thenReturn(sampleContentData)
        `when`(exampleQueryMapper.toDto(any<ExampleContentData>())).thenReturn(sampleContentDto)

        // when
        exampleQueryService.selectExample(1L)
        exampleQueryService.selectExample(2L)
        exampleQueryService.getContents("content-1")

        // then
        verify(exampleQueryMapper, times(2)).toDto(any<ExampleView>())
        verify(exampleQueryMapper, times(1)).toDto(any<ExampleContentData>())
    }
}
