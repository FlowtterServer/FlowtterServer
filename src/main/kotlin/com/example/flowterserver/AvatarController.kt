package com.example.flowterserver.controller

import com.example.flowterserver.service.AvatarStorageService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/avatars")
class AvatarController(
    private val avatarStorageService: AvatarStorageService
) {

    @PostMapping
    fun uploadAvatar(
        @RequestParam("userId") userId: Long,
        @RequestParam("file") file: MultipartFile
    ): Map<String, String> {

        require(!file.isEmpty) {
            "Le fichier est vide"
        }

        val avatarUrl = avatarStorageService.uploadAvatar(
            userId = userId,
            file = file
        )

        return mapOf(
            "avatarUrl" to avatarUrl
        )
    }
}