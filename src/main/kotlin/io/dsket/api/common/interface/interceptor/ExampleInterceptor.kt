package io.dsket.api.common.`interface`.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView

@Component
class ExampleInterceptor : HandlerInterceptor {
    
    private val logger = LoggerFactory.getLogger(ExampleInterceptor::class.java)
    
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val method = request.method
        val uri = request.requestURI
        val userAgent = request.getHeader("User-Agent") ?: "Unknown"
        
        logger.info("Pre-handle: {} {} - User-Agent: {}", method, uri, userAgent)
        
        // 요청 시작 시간을 request attribute에 저장
        request.setAttribute("startTime", System.currentTimeMillis())
        
        // true를 반환하면 다음 단계로 진행, false면 요청 중단
        return true
    }
    
    override fun postHandle(
        request: HttpServletRequest, 
        response: HttpServletResponse, 
        handler: Any, 
        modelAndView: ModelAndView?
    ) {
        val method = request.method
        val uri = request.requestURI
        
        logger.info("Post-handle: {} {} - Controller processing completed", method, uri)
    }
    
    override fun afterCompletion(
        request: HttpServletRequest, 
        response: HttpServletResponse, 
        handler: Any, 
        ex: Exception?
    ) {
        val method = request.method
        val uri = request.requestURI
        val status = response.status
        val startTime = request.getAttribute("startTime") as? Long
        
        val duration = if (startTime != null) {
            System.currentTimeMillis() - startTime
        } else {
            -1
        }
        
        if (ex != null) {
            logger.error("After-completion: {} {} - Status: {} - Duration: {}ms - Exception: {}", 
                method, uri, status, duration, ex.message)
        } else {
            logger.info("After-completion: {} {} - Status: {} - Duration: {}ms", 
                method, uri, status, duration)
        }
    }
}