package com.example.flowterserver.service

import com.example.flowterserver.model.VoiceMessage
import com.example.flowterserver.repository.VoiceMessageRepository
import org.springframework.stereotype.Service

@Service
class VoiceMessageService(
    private val voiceMessageRepository: VoiceMessageRepository,
    private val supabaseStorageService: SupabaseStorageService
) {

    fun sendVoiceMessage(
        senderId: Long,
        receiverId: Long,
        audioBytes: ByteArray,
        contentType: String
    ): VoiceMessage {

        if (audioBytes.isEmpty()) {
            throw IllegalArgumentException("Audio file is empty")
        }

        val fileName = supabaseStorageService.uploadVoiceMessage(
            audioBytes = audioBytes,
            contentType = contentType
        )

        val voiceMessage = VoiceMessage(
            senderId = senderId,
            receiverId = receiverId,
            fileName = fileName,
            contentType = contentType
        )

        return voiceMessageRepository.save(voiceMessage)
    }

    fun getConversation(
        user1: Long,
        user2: Long
    ): List<VoiceMessage> {
        return voiceMessageRepository.findConversation(
            user1 = user1,
            user2 = user2
        )
    }
}