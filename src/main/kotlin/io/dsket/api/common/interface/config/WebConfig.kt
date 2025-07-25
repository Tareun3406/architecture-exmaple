package io.dsket.api.common.`interface`.config

import io.dsket.api.common.`interface`.filter.ExampleFilter
import io.dsket.api.common.`interface`.interceptor.ExampleInterceptor
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val exampleInterceptor: ExampleInterceptor
) : WebMvcConfigurer {

    /**
     * Interceptor 등록
     * Spring MVC 레벨에서 동작하며, Controller 매핑된 요청에만 적용
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(exampleInterceptor)
            .addPathPatterns("/**")  // 모든 경로에 적용
            .excludePathPatterns(    // 제외할 경로들
                "/actuator/**",      // Spring Boot Actuator
                "/swagger-ui/**",    // Swagger UI
                "/v3/api-docs/**",   // OpenAPI docs
                "/favicon.ico"       // 파비콘
            )
    }

    /**
     * Filter 등록 (선택사항)
     * @Component로 이미 자동 등록되지만, 세밀한 제어가 필요한 경우 사용
     */
    @Bean
    fun exampleFilterRegistration(exampleFilter: ExampleFilter): FilterRegistrationBean<ExampleFilter> {
        val registration = FilterRegistrationBean<ExampleFilter>()
        registration.filter = exampleFilter
        registration.addUrlPatterns("/*")  // 모든 URL에 적용
        registration.order = 1             // 필터 실행 순서 (낮을수록 먼저 실행)
        registration.setName("ExampleFilter")
        return registration
    }
}
