package com.example.flowterserver.repository

import com.example.flowterserver.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
}