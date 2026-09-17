package com.example.flowterserver

import com.example.flowterserver.service.AuthService
import com.example.flowterserver.service.JwtService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val jwtService: JwtService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): Map<String, Any?> {

        val user = authService.register(
            username = request.username,
            email = request.email,
            password = request.password
        )

        return mapOf(
            "id" to user.id,
            "username" to user.username,
            "email" to user.email
        )
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): Map<String, Any?> {

        val user = authService.login(
            username = request.username,
            password = request.password
        )

        val token = jwtService.generateToken(user.username)

        return mapOf(
            "id" to user.id,
            "username" to user.username,
            "email" to user.email,
            "token" to token
        )
    }
}