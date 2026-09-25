package com.example.flowterserver.service

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.UUID

data class SupabaseSignedUrlResponse(
    @JsonProperty("signedURL")
    val signedURL: String
)

@Service
class SupabaseStorageService {

    @Value("\${SUPABASE_URL}")
    private lateinit var supabaseUrl: String

    @Value("\${SUPABASE_SECRET_KEY}")
    private lateinit var supabaseSecretKey: String

    private val restTemplate = RestTemplate()

    fun uploadVoiceMessage(
        audioBytes: ByteArray,
        contentType: String
    ): String {

        val fileName = "${UUID.randomUUID()}.m4a"

        val url =
            "$supabaseUrl/storage/v1/object/voice-messages/$fileName"

        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType(contentType)

        headers.set("Authorization", "Bearer $supabaseSecretKey")
        headers.set("apikey", supabaseSecretKey)
        headers.set("x-upsert", "false")

        val resource = object : ByteArrayResource(audioBytes) {
            override fun getFilename(): String = fileName
        }

        val request = HttpEntity(resource, headers)

        restTemplate.postForEntity(
            url,
            request,
            String::class.java
        )

        return fileName
    }

    fun createVoiceMessageSignedUrl(
        fileName: String
    ): String {

        val url =
            "$supabaseUrl/storage/v1/object/sign/voice-messages/$fileName"

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("Authorization", "Bearer $supabaseSecretKey")
        headers.set("apikey", supabaseSecretKey)

        val body = mapOf(
            "expiresIn" to 3600
        )

        val request = HttpEntity(body, headers)

        val response = restTemplate.postForObject(
            url,
            request,
            SupabaseSignedUrlResponse::class.java
        ) ?: throw IllegalStateException(
            "Supabase did not return a signed URL"
        )

        val signedPath = response.signedURL

        return if (signedPath.startsWith("http")) {
            signedPath
        } else {
            "$supabaseUrl/storage/v1$signedPath"
        }
    }
}