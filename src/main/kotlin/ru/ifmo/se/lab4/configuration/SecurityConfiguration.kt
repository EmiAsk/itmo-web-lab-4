package ru.ifmo.se.lab4.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.handler.HandlerMappingIntrospector
import ru.ifmo.se.lab4.authentication.JwtFilter
import ru.ifmo.se.lab4.authentication.JwtUtils

@EnableWebSecurity
@Configuration
class SecurityConfiguration(private val jwt: JwtUtils) {
    @Bean
    fun filterChain(http: HttpSecurity, introspection: HandlerMappingIntrospector?): SecurityFilterChain {
        http
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .sessionManagement { configurer: SessionManagementConfigurer<HttpSecurity?> ->
                configurer.sessionCreationPolicy(
                    STATELESS
                )
            }
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/login")
                    .permitAll()
                    .requestMatchers("/register")
                    .permitAll()
                    .requestMatchers("/attempt").authenticated()
                    .anyRequest().permitAll()
            }
            .addFilterBefore(JwtFilter(jwt), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
