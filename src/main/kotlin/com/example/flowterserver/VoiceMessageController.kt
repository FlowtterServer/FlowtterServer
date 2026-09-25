package com.example.flowterserver

import com.example.flowterserver.model.VoiceMessage
import com.example.flowterserver.service.VoiceMessageService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/voice-messages")
class VoiceMessageController(
    private val voiceMessageService: VoiceMessageService
) {

    @PostMapping(
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadVoiceMessage(
        @RequestParam senderId: Long,
        @RequestParam receiverId: Long,
        @RequestPart("file") file: MultipartFile
    ): VoiceMessage {

        if (file.isEmpty) {
            throw IllegalArgumentException("Audio file is empty")
        }

        val contentType = file.contentType ?: "audio/mp4"

        if (!contentType.startsWith("audio/")) {
            throw IllegalArgumentException("Only audio files are allowed")
        }

        return voiceMessageService.sendVoiceMessage(
            senderId = senderId,
            receiverId = receiverId,
            audioBytes = file.bytes,
            contentType = contentType
        )
    }

    @GetMapping
    fun getConversation(
        @RequestParam senderId: Long,
        @RequestParam receiverId: Long
    ): List<VoiceMessage> {

        return voiceMessageService.getConversation(
            user1 = senderId,
            user2 = receiverId
        )
    }
}