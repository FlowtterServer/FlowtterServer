
package com.example.flowterserver.service

import com.example.flowterserver.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile

@Service
class AvatarStorageService(

    @Value("\${supabase.url}")
    private val supabaseUrl: String,

    @Value("\${supabase.key}")
    private val supabaseKey: String,

    private val userRepository: UserRepository

) {

    private val restTemplate = RestTemplate()

    fun uploadAvatar(
        userId: Long,
        file: MultipartFile
    ): String {

        require(!file.isEmpty) {
            "Le fichier est vide"
        }

        // Vérifier que l'utilisateur existe
        val user = userRepository.findById(userId)
            .orElseThrow {
                IllegalArgumentException("Utilisateur introuvable : $userId")
            }

        // Préparer le nom du fichier
        val extension = when (file.contentType) {
            "image/png" -> "png"
            "image/webp" -> "webp"
            "image/gif" -> "gif"
            else -> "jpg"
        }

        val fileName = "$userId-${System.currentTimeMillis()}.$extension"

        // URL d'envoi vers Supabase Storage
        val cleanSupabaseUrl = supabaseUrl.trimEnd('/')

        val uploadUrl =
            "$cleanSupabaseUrl/storage/v1/object/avatars/$fileName"

        // Préparer les en-têtes HTTP
        val headers = HttpHeaders().apply {
            setBearerAuth(supabaseKey)
            set("apikey", supabaseKey)
            contentType = MediaType.APPLICATION_OCTET_STREAM
            set("x-upsert", "true")
        }

        // Préparer le fichier
        val entity = HttpEntity(
            file.bytes,
            headers
        )

        // Envoyer le fichier vers Supabase
        restTemplate.exchange(
            uploadUrl,
            HttpMethod.POST,
            entity,
            String::class.java
        )

        // Construire l'URL publique
        val avatarUrl =
            "$cleanSupabaseUrl/storage/v1/object/public/avatars/$fileName"

        require(avatarUrl.startsWith("https://")) {
            "L'URL de l'avatar n'est pas sécurisée"
        }

        // Enregistrer l'URL dans la base de données
        user.avatarUrl = avatarUrl

        userRepository.save(user)

        // Vérifier la sauvegarde
        val savedUser = userRepository.findById(userId)
            .orElseThrow {
                IllegalStateException("Utilisateur introuvable après sauvegarde")
            }

        check(savedUser.avatarUrl == avatarUrl) {
            "L'URL de l'avatar n'a pas été correctement enregistrée"
        }

        return avatarUrl
    }
}
