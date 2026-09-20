package com.example.flowterserver.service

import com.example.flowterserver.model.Message
import com.example.flowterserver.repository.MessageRepository
import org.springframework.stereotype.Service

@Service
class MessageService(
    private val messageRepository: MessageRepository
) {

    fun sendMessage(
        senderId: Long,
        receiverId: Long,
        content: String
    ): Message {

        if (content.isBlank()) {
            throw IllegalArgumentException("Message cannot be empty")
        }

        val message = Message(
            senderId = senderId,
            receiverId = receiverId,
            content = content.trim()
        )

        return messageRepository.save(message)
    }

    fun getConversation(
        senderId: Long,
        receiverId: Long
    ): List<Message> {

        return messageRepository
            .findBySenderIdAndReceiverIdOrderByCreatedAtAsc(
                senderId,
                receiverId
            )
    }
}