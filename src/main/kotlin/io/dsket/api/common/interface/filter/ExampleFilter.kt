package io.dsket.api.common.`interface`.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ExampleFilter: Filter {

    private val logger = LoggerFactory.getLogger(ExampleFilter::class.java)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        val startTime = System.currentTimeMillis()
        val method = httpRequest.method
        val uri = httpRequest.requestURI

        logger.info("Request started: {} {}", method, uri)

        try {
            chain.doFilter(request, response)
        } finally {
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime
            val status = httpResponse.status

            logger.info("Request completed: {} {} - Status: {} - Duration: {}ms",
                method, uri, status, duration)
        }
    }
}