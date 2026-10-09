
package com.example.flowterserver

import com.example.flowterserver.repository.UserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userRepository: UserRepository
) {

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): Map<String, Any?> {
        val user = userRepository.findById(id)
            .orElseThrow {
                NoSuchElementException("Utilisateur introuvable")
            }

        return mapOf(
            "id" to user.id,
            "username" to user.username,
            "name" to user.name,
            "avatarUrl" to user.avatarUrl
        )
    }
}