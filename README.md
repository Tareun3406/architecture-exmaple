# 사이드 프로젝트에서 팀에 공유하기 위해 만든 예제 코드입니다.
# Clean Architecture + CQRS + DDD 아키텍처 가이드

## 📋 목차
1. [개요](#-개요)
2. [아키텍처 원칙](#-아키텍처-원칙)
3. [계층 구조](#-계층-구조)
4. [패키지 구조](#-패키지-구조)
5. [핵심 패턴](#-핵심-패턴)
6. [예외 처리](#-예외-처리)
7. [테스트 전략](#-테스트-전략)
8. [개발 가이드라인](#-개발-가이드라인)

---

## 🎯 개요

본 아키텍처는 **Clean Architecture**, **CQRS**, **DDD** 패턴을 결합하여 확장 가능하고 유지보수가 용이한 백엔드 시스템을 구축하기 위해 설계되었습니다.

### 핵심 목표
- **관심사의 분리**: 각 계층이 명확한 책임을 가짐
- **의존성 역전**: 고수준 모듈이 저수준 모듈에 의존하지 않음
- **테스트 용이성**: 각 계층을 독립적으로 테스트 가능
- **확장성**: 새로운 기능 추가 시 기존 코드에 최소한의 영향
- **도메인 중심**: 비즈니스 로직이 기술적 관심사와 분리

---

## 🏗️ 아키텍처 원칙

### 1. 의존성 규칙
```
Interface → Application → Domain ← Infrastructure
```
- **Domain**: 다른 계층에 의존하지 않음 (순수한 비즈니스 로직)
- **Application**: Domain에만 의존
- **Infrastructure**: Domain 인터페이스를 구현
- **Interface**: Application을 호출

### 2. CQRS 분리
- **Command**: 데이터 변경 작업 (Create, Update, Delete)
- **Query**: 데이터 조회 작업 (Read, Search)
- **Repository 분리**: CommandRepository ↔ QueryRepository

### 3. DDD 패턴
- **Aggregate**: 도메인 객체의 일관성 경계
- **Entity**: 고유 식별자를 가진 도메인 객체
- **Value Object**: 불변 값 객체
- **Domain Service**: 여러 Entity를 조합하는 도메인 로직

---

## 🏛️ 계층 구조

### Interface Layer (인터페이스 계층)
**역할**: 외부 세계와의 소통 담당
```kotlin
// 예시: REST API Controller
@RestController
class ExampleController(
    private val exampleFacade: ExampleFacade
) {
    @PostMapping
    fun createExample(@RequestBody request: ExampleRequest): ExampleResponse
}
```

**구성 요소**:
- **Controller**: REST API 엔드포인트
- **DTO**: HTTP 요청/응답 객체
- **Mapper**: Web ↔ Application 변환
- **Filter/Interceptor**: 횡단 관심사

### Application Layer (애플리케이션 계층)
**역할**: 유스케이스 조합 및 트랜잭션 관리
```kotlin
// 예시: Command Service
@Service
class ExampleCommandService(
    private val exampleRepository: ExampleCommandRepository
) {
    fun createExample(command: ExampleCreateCommand): ExampleResult {
        val example = Example.create(command.name)
        return exampleRepository.save(example)
    }
}
```

**구성 요소**:
- **Facade**: 유스케이스 진입점
- **Service**: 비즈니스 로직 조합
- **Command/Query**: CQRS 패턴 구현
- **DTO**: 계층 간 데이터 전송

### Domain Layer (도메인 계층)
**역할**: 핵심 비즈니스 로직 및 규칙
```kotlin
// 예시: Domain Entity
class Example(
    val id: Long,
    private var name: String
) {
    fun updateName(newName: String) {
        if (newName.isBlank()) {
            throw BaseException(ErrorCode.INVALID_NAME)
        }
        this.name = newName
    }
}
```

**구성 요소**:
- **Entity**: 도메인 객체
- **Repository Interface**: 저장소 추상화
- **Gateway Interface**: 외부 시스템 추상화
- **Domain Service**: 도메인 로직

### Infrastructure Layer (인프라스트럭처 계층)
**역할**: 기술적 구현 및 외부 시스템 연동
```kotlin
// 예시: Repository 구현
@Repository
class ExampleRepositoryImpl(
    private val jpaRepository: ExampleJpaRepository
) : ExampleCommandRepository {
    override fun save(example: Example): Example {
        val entity = mapper.toEntity(example)
        return mapper.toDomain(jpaRepository.save(entity))
    }
}
```

**구성 요소**:
- **Repository 구현**: 데이터 영속성
- **Gateway 구현**: 외부 API 연동
- **Entity**: JPA 엔티티
- **Configuration**: 기술 설정

---

## 📁 패키지 구조

```
src/main/kotlin/io/dsket/api/
├── common/                                     # 🔧 공통 모듈
│   ├── interface/                              # 웹 계층 공통
│   │   ├── config/                             # 설정
│   │   ├── filter/                             # 전역 필터
│   │   ├── interceptor/                        # 전역 인터셉터
│   │   └── exception/                          # 예외 처리
│   └── infrastructure/                         # 인프라 공통
│       └── external/                           # 외부 시스템 클라이언트
│
└── {domain}/                                   # 📋 도메인별 모듈
    ├── interface/                              # 🌐 Interface Layer
    │   └── web/
    │       ├── controller/                     # REST Controller
    │       ├── dto/                            # HTTP DTO
    │       └── mapper/                         # Web Mapper
    │
    ├── application/                            # 🎯 Application Layer
    │   ├── facade/                             # 유스케이스 진입점
    │   │   ├── dto/                            # Facade DTO
    │   │   └── mapper/                         # Facade Mapper
    │   └── service/                            # 애플리케이션 서비스
    │       ├── command/                        # 명령 처리
    │       └── query/                          # 조회 처리
    │
    ├── domain/                                 # 🏛️ Domain Layer
    │   ├── model/                              # 도메인 엔티티
    │   ├── repository/                         # 저장소 인터페이스
    │   ├── gateway/                            # 외부 시스템 인터페이스
    │   ├── service/                            # 도메인 서비스
    │   └── data/                               # Gateway 데이터
    │
    └── infrastructure/                         # 🔧 Infrastructure Layer
        ├── persistence/                        # 데이터 영속성
        │   ├── jpa/                            # JPA 관련
        │   └── mapper/                         # 인프라 매퍼
        └── external/                           # 외부 시스템 연동
            └── mapper/                         # 외부 매퍼
```

---

## 🔄 핵심 패턴

### 1. CQRS (Command Query Responsibility Segregation)

#### Command Side (명령)
```kotlin
// Command DTO
data class ExampleCreateCommand(
    val name: String
)

// Command Service
@Service
class ExampleCommandService(
    private val repository: ExampleCommandRepository
) {
    fun createExample(command: ExampleCreateCommand): ExampleResult {
        val example = Example.create(command.name)
        return repository.save(example)
    }
}

// Command Repository
interface ExampleCommandRepository {
    fun save(example: Example): Example
    fun getReferenceById(id: Long): Example
}
```

#### Query Side (조회)
```kotlin
// Query DTO
data class ExampleDto(
    val id: Long,
    val name: String
)

// Query Service
@Service
class ExampleQueryService(
    private val repository: ExampleQueryRepository
) {
    fun getExample(id: Long): ExampleDto {
        val view = repository.getViewById(id)
        return mapper.toDto(view)
    }
}

// Query Repository
interface ExampleQueryRepository {
    fun getViewById(id: Long): ExampleView
}
```

### 2. Gateway 패턴 (외부 시스템 연동)

#### Domain Layer
```kotlin
// Gateway Interface (도메인)
interface ExampleGateway {
    fun getContent(id: String): ExampleContentData
}

// Gateway Data (도메인)
data class ExampleContentData(
    val content: String
)
```

#### Infrastructure Layer
```kotlin
// Gateway 구현체
@Component
class ExampleGatewayImpl(
    private val notionClient: NotionClient,
    private val mapper: ExampleGatewayMapper
) : ExampleGateway {
    override fun getContent(id: String): ExampleContentData {
        val response = notionClient.getContent(id)
        return mapper.toData(response)
    }
}
```

### 3. Mapper 패턴 (계층 간 변환)

```kotlin
@Component
class ExampleFacadeMapper {
    // Web → Application
    fun toCommand(input: ExampleCreateInput): ExampleCreateCommand {
        return ExampleCreateCommand(name = input.name)
    }
    
    // Application → Web
    fun toOutput(result: ExampleResult): ExampleOutput {
        return ExampleOutput(id = result.id, name = result.name)
    }
}
```

---

## ⚠️ 예외 처리

### 구조
```
common/interface/exception/
├── BaseException.kt          # 공통 예외 클래스
├── ErrorCode.kt             # 에러 코드 + HTTP 상태
├── ExceptionResponse.kt     # API 응답 모델
└── GlobalExceptionHandler.kt # 전역 예외 처리기
```

### 구현
```kotlin
// 에러 코드 정의
enum class ErrorCode(
    val httpStatus: HttpStatus,
    val message: String
) {
    // Domain Errors
    EXAMPLE_NAME_EMPTY(HttpStatus.BAD_REQUEST, "이름이 비어있습니다"),
    EXAMPLE_NOT_FOUND(HttpStatus.NOT_FOUND, "Example을 찾을 수 없습니다"),
    
    // System Errors
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다")
}

// 공통 예외 클래스
class BaseException(
    val errorCode: ErrorCode,
    cause: Throwable? = null
) : RuntimeException(cause)

// 전역 예외 처리
@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BaseException::class)
    fun handleBaseException(ex: BaseException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(ex.errorCode.message)
        return ResponseEntity(response, ex.errorCode.httpStatus)
    }
}
```

### 사용법
```kotlin
// Domain에서 예외 발생
class Example {
    fun updateName(name: String) {
        if (name.isBlank()) {
            throw BaseException(ErrorCode.EXAMPLE_NAME_EMPTY)
        }
        this.name = name
    }
}
```

---

## 🧪 테스트 전략

### 테스트 피라미드
```
Integration Tests (소수)
    ↑
Service Tests (적당)
    ↑
Domain Tests (다수)
```

### 1. Domain Tests (필수)
```kotlin
@DisplayName("Example 도메인 테스트")
class ExampleTest {
    @Test
    fun `이름 업데이트 성공`() {
        // given
        val example = Example(1L, "원래이름")
        
        // when
        example.updateName("새이름")
        
        // then
        assertEquals("새이름", example.name)
    }
    
    @Test
    fun `빈 이름으로 업데이트 시 예외 발생`() {
        // given
        val example = Example(1L, "원래이름")
        
        // when & then
        assertThrows<BaseException> {
            example.updateName("")
        }
    }
}
```

### 2. Application Service Tests (권장)
```kotlin
@ExtendWith(MockitoExtension::class)
class ExampleCommandServiceTest {
    @Mock
    private lateinit var repository: ExampleCommandRepository
    
    @InjectMocks
    private lateinit var service: ExampleCommandService
    
    @Test
    fun `Example 생성 성공`() {
        // given
        val command = ExampleCreateCommand("테스트")
        val example = Example(1L, "테스트")
        `when`(repository.save(any())).thenReturn(example)
        
        // when
        val result = service.createExample(command)
        
        // then
        assertEquals("테스트", result.name)
        verify(repository).save(any())
    }
}
```

### 3. Integration Tests (선택적)
```kotlin
@SpringBootTest
@Testcontainers
class ExampleIntegrationTest {
    @Test
    fun `전체 플로우 테스트`() {
        // given
        val request = ExampleCreateRequest("통합테스트")
        
        // when
        val response = restTemplate.postForObject("/examples", request, ExampleResponse::class.java)
        
        // then
        assertNotNull(response)
        assertEquals("통합테스트", response.name)
    }
}
```

---

## 📝 개발 가이드라인

### 1. 네이밍 컨벤션

#### DTO 네이밍
- **Request/Response**: Infrastructure 계층 (HTTP, 외부 API)
- **Input/Output**: Application 계층 (Facade)
- **Command/Query**: Application 계층 (Service)
- **Data**: Domain 계층 (Gateway)
- **View**: Application 계층 (DB Projection)

#### 예시
```
// Web Layer
ExampleCreateRequest → ExampleCreateInput
ExampleResponse → ExampleOutput

// Application Layer
-- facade  
ExampleCreateInput → ExampleCreateCommand
ExampleResult → ExampleOutput
ExampleSelectInput -> ExampleSelectQuery
ExampleDto -> ExampleOutput

-- service 
Example -> ExampleResult / ExampleCommandResult
ExampleView -> ExampleResult / ExampleQueryResult
ExampleContentData-> ExampleResult / ExampleContentQueryResult

// Domain Layer
ExampleContentData (Gateway용)
Example (Domain)

// Infrastructure Layer
NotionApiResponse (외부 API)
NotionApiRequest (외부 API)
ExampleEntity (DB Entity) -> Example
```

### 2. 의존성 주입
```kotlin
// Constructor Injection 사용
@Service
class ExampleService(
    private val repository: ExampleRepository,
    private val mapper: ExampleMapper
) {
    // 구현
}
```

### 3. 매퍼 구현
```kotlin
@Component
class ExampleMapper {
    fun toCommand(input: ExampleInput): ExampleCommand {
        return ExampleCommand(
            name = input.name,
            description = input.description
        )
    }
}
```

### 4. 예외 처리
```kotlin
// 도메인에서 비즈니스 예외 발생
if (name.isBlank()) {
    throw BaseException(ErrorCode.INVALID_NAME)
}

// 인프라에서 기술적 예외 처리
try {
    return externalApi.call()
} catch (e: Exception) {
    throw BaseException(ErrorCode.EXTERNAL_API_ERROR, e)
}
```

---
## 🎯 결론

본 아키텍처는 다음과 같은 이점을 제공합니다:

### 장점
- **유지보수성**: 관심사 분리로 변경 영향 최소화
- **테스트 용이성**: 각 계층을 독립적으로 테스트
- **확장성**: 새로운 기능 추가 시 기존 코드 영향 최소
- **팀 협업**: 명확한 구조로 개발자 간 협업 효율성 증대

### 고려사항
- **초기 복잡성**: 작은 프로젝트에는 과도할 수 있음
- **학습 곡선**: 팀원들의 아키텍처 이해 필요
- **코드량 증가**: 계층 분리로 인한 보일러플레이트 코드

---
