package com.example.flowterserver.repository

import com.example.flowterserver.model.VoiceMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface VoiceMessageRepository : JpaRepository<VoiceMessage, Long> {

    @Query(
        """
        SELECT v
        FROM VoiceMessage v
        WHERE (v.senderId = :user1 AND v.receiverId = :user2)
           OR (v.senderId = :user2 AND v.receiverId = :user1)
        ORDER BY v.createdAt ASC
        """
    )
    fun findConversation(
        @Param("user1") user1: Long,
        @Param("user2") user2: Long
    ): List<VoiceMessage>
}