package ru.ifmo.se.lab4

import jakarta.servlet.FilterChain
import jakarta.servlet.GenericFilter
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder

class JwtFilter(private val jwt: JwtUtils) : GenericFilter() {
    override fun doFilter(
        servletRequest: ServletRequest,
        servletResponse: ServletResponse,
        filterChain: FilterChain
    ) {
        val token = jwt.resolveToken((servletRequest as HttpServletRequest))
        if (token != null && jwt.validateToken(token)) {
            val auth = jwt.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = auth
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }
}
