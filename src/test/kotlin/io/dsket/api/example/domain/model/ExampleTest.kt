package io.dsket.api.example.domain.model

import io.dsket.api.common.`interface`.exception.BaseException
import io.dsket.api.common.`interface`.exception.ErrorCode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("Example 도메인 엔티티 테스트")
class ExampleTest {

    @Test
    @DisplayName("Example 객체 생성 - 성공")
    fun `should create Example with valid parameters`() {
        // given
        val id = 1L
        val name = "Test Example"

        // when
        val example = Example(id = id, name = name)

        // then
        assertEquals(id, example.id)
        assertEquals(name, example.name)
    }

    @Test
    @DisplayName("Example 객체 생성 - 기본값으로 생성")
    fun `should create Example with default id`() {
        // given
        val name = "Test Example"

        // when
        val example = Example(name = name)

        // then
        assertEquals(0L, example.id)
        assertEquals(name, example.name)
    }

    @Test
    @DisplayName("이름 업데이트 - 성공")
    fun `should update name successfully`() {
        // given
        val example = Example(id = 1L, name = "Original Name")
        val newName = "Updated Name"

        // when
        example.updateName(newName)

        // then
        assertEquals(newName, example.name)
    }

    @Test
    @DisplayName("이름 업데이트 - 빈 문자열로 업데이트 시 예외 발생")
    fun `should throw exception when updating with empty name`() {
        // given
        val example = Example(id = 1L, name = "Original Name")
        val emptyName = ""

        // when & then
        val exception = assertThrows<BaseException> {
            example.updateName(emptyName)
        }

        assertEquals(ErrorCode.ExampleDomain_0001, exception.errorCode)
        assertEquals("name is empty", exception.errorCode.message)
        
        // 원래 이름이 변경되지 않았는지 확인
        assertEquals("Original Name", example.name)
    }

    @Test
    @DisplayName("이름 업데이트 - 공백 문자열로 업데이트 시 예외 발생")
    fun `should throw exception when updating with blank name`() {
        // given
        val example = Example(id = 1L, name = "Original Name")
        val blankName = "   "

        // when & then
        val exception = assertThrows<BaseException> {
            example.updateName(blankName)
        }

        assertEquals(ErrorCode.ExampleDomain_0001, exception.errorCode)
        
        // 원래 이름이 변경되지 않았는지 확인
        assertEquals("Original Name", example.name)
    }

    @Test
    @DisplayName("이름 업데이트 - 여러 번 연속 업데이트")
    fun `should update name multiple times`() {
        // given
        val example = Example(id = 1L, name = "Original Name")

        // when
        example.updateName("First Update")
        example.updateName("Second Update")
        example.updateName("Final Update")

        // then
        assertEquals("Final Update", example.name)
    }

    @Test
    @DisplayName("이름 업데이트 - 특수문자 포함 이름으로 업데이트")
    fun `should update name with special characters`() {
        // given
        val example = Example(id = 1L, name = "Original Name")
        val specialName = "Test@#$%^&*()_+-=[]{}|;':\",./<>?"

        // when
        example.updateName(specialName)

        // then
        assertEquals(specialName, example.name)
    }

    @Test
    @DisplayName("이름 업데이트 - 긴 이름으로 업데이트")
    fun `should update name with long string`() {
        // given
        val example = Example(id = 1L, name = "Original Name")
        val longName = "A".repeat(1000)

        // when
        example.updateName(longName)

        // then
        assertEquals(longName, example.name)
        assertEquals(1000, example.name.length)
    }

    @Test
    @DisplayName("도메인 불변성 검증 - ID는 변경되지 않음")
    fun `should maintain id immutability`() {
        // given
        val originalId = 1L
        val example = Example(id = originalId, name = "Test Name")

        // when
        example.updateName("Updated Name")

        // then
        assertEquals(originalId, example.id) // ID는 변경되지 않아야 함
    }
}
