package com.inner.circle.api.security

import com.inner.circle.core.security.AccountValidationProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class UserApiAuthenticationFilter(
    private val provider: AccountValidationProvider,
    private val authenticationEntryPoint: CustomAuthenticationEntryPoint
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
            val token = authHeader.removePrefix(BEARER_AUTH_TOKEN_PREFIX).trim()

            SecurityContextHolder.getContext().authentication =
                provider.getUserValidAuthenticationOrThrow(token = token)

            filterChain.doFilter(request, response)
        } catch (ex: Exception) {
            SecurityContextHolder.clearContext()
            authenticationEntryPoint.commence(
                request,
                response,
                BadCredentialsException(ex.message)
            )
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.servletPath
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        return authHeader == null ||
            !authHeader.startsWith(BEARER_AUTH_TOKEN_PREFIX) ||
            path == "/api/v1/p/user/sign-in" ||
            path == "/api/v1/p/user/sign-up"
    }

    companion object {
        private const val BEARER_AUTH_TOKEN_PREFIX = "Bearer "
    }
}
