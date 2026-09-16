package com.example.flowterserver.service

import com.example.flowterserver.model.User
import com.example.flowterserver.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository
) {

    private val passwordEncoder = BCryptPasswordEncoder()

    fun register(username: String, email: String, password: String): User {

        if (userRepository.findByUsername(username) != null) {
            throw IllegalArgumentException("Username already exists")
        }

        if (userRepository.findByEmail(email) != null) {
            throw IllegalArgumentException("Email already exists")
        }

        val user = User(
            username = username,
            email = email,
            password = requireNotNull(passwordEncoder.encode(password))
        )

        return userRepository.save(user)
    }
}
