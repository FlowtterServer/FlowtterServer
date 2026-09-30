package com.example.flowterserver.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile

@Service
class AvatarStorageService(

    @Value("\${supabase.url}")
    private val supabaseUrl: String,

    @Value("\${supabase.key}")
    private val supabaseKey: String
) {

    private val restTemplate = RestTemplate()

    fun uploadAvatar(
        userId: Long,
        file: MultipartFile
    ): String {

        val fileName = "$userId-${System.currentTimeMillis()}.jpg"

        val url =
            "$supabaseUrl/storage/v1/object/avatars/$fileName"

        val headers = HttpHeaders()

        headers.set(
            "Authorization",
            "Bearer $supabaseKey"
        )

        headers.set(
            "apikey",
            supabaseKey
        )

        headers.contentType =
            org.springframework.http.MediaType.APPLICATION_OCTET_STREAM

        val entity =
            HttpEntity(
                file.bytes,
                headers
            )

        restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            String::class.java
        )

        return "$supabaseUrl/storage/v1/object/public/avatars/$fileName"
    }
}