package com.example.flowterserver

import com.example.flowterserver.service.SupabaseStorageService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/voice-messages")
class VoiceMessageController(
    private val supabaseStorageService: SupabaseStorageService
) {

    @PostMapping(
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadVoiceMessage(
        @RequestPart("file") file: MultipartFile
    ): Map<String, Any?> {

        if (file.isEmpty) {
            throw IllegalArgumentException("Audio file is empty")
        }

        val contentType = file.contentType ?: "audio/mp4"

        if (!contentType.startsWith("audio/")) {
            throw IllegalArgumentException("Only audio files are allowed")
        }

        val fileName = supabaseStorageService.uploadVoiceMessage(
            audioBytes = file.bytes,
            contentType = contentType
        )

        return mapOf(
            "success" to true,
            "fileName" to fileName
        )
    }
}