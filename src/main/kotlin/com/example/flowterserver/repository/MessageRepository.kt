package com.example.flowterserver.repository

import com.example.flowterserver.model.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MessageRepository : JpaRepository<Message, Long> {

    @Query(
        """
        SELECT m
        FROM Message m
        WHERE (m.senderId = :user1 AND m.receiverId = :user2)
           OR (m.senderId = :user2 AND m.receiverId = :user1)
        ORDER BY m.createdAt ASC
        """
    )
    fun findConversation(
        @Param("user1") user1: Long,
        @Param("user2") user2: Long
    ): List<Message>
}