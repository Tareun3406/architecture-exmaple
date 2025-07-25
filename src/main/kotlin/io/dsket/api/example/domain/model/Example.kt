package io.dsket.api.example.domain.model

import io.dsket.api.common.`interface`.exception.BaseException
import io.dsket.api.common.`interface`.exception.ErrorCode

class Example( // fixme DDD 에서의 Entity(도메인 객체)
    val id: Long = 0,
    var name: String // fixme var 로 선언한 경우 외부에서 example.name = "test' 형식으로 수정 가능. 정책적으로 사용 금지 or 명시적으로 막고, 별도의 메서드를 선언 할 것.
) {
    fun updateName(name: String) { // fixme '이름을 수정 한다' 에 대한 실제 구현. 이름은 도메인(Example)의 속하기 때문에 여기서 비즈니스를 관리함.
        if (name.isBlank()) throw BaseException(ErrorCode.ExampleDomain_0001)
        this.name = name
    }
}