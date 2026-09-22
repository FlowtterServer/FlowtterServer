package com.example.flowterserver

import com.example.flowterserver.model.Message
import com.example.flowterserver.service.MessageService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

data class SendMessageRequest(
    val senderId: Long,
    val receiverId: Long,
    val content: String
)

@RestController
@RequestMapping("/api/messages")
class MessageController(
    private val messageService: MessageService
) {

    @PostMapping
    fun sendMessage(
        @RequestBody request: SendMessageRequest
    ): Message {

        return messageService.sendMessage(
            senderId = request.senderId,
            receiverId = request.receiverId,
            content = request.content
        )
    }

    @GetMapping
    fun getConversation(
        @RequestParam senderId: Long,
        @RequestParam receiverId: Long
    ): List<Message> {

        return messageService.getConversation(
            user1 = senderId,
            user2 = receiverId
        )
    }
}