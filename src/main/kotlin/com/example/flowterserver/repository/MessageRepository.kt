package com.example.flowterserver.repository

import com.example.flowterserver.model.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository : JpaRepository<Message, Long> {

    fun findBySenderIdAndReceiverIdOrderByCreatedAtAsc(
        senderId: Long,
        receiverId: Long
    ): List<Message>
}