package com.example.flowterserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf ->
                csrf.disable()
            }
            .formLogin { formLogin ->
                formLogin.disable()
            }
            .httpBasic { httpBasic ->
                httpBasic.disable()
            }
            .logout { logout ->
                logout.disable()
            }
            .authorizeHttpRequests { auth ->
                auth
                    .anyRequest()
                    .permitAll()
            }

        return http.build()
    }
}