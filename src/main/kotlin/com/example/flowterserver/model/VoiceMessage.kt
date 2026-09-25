package com.example.flowterserver.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "voice_messages")
class VoiceMessage(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var senderId: Long = 0,

    @Column(nullable = false)
    var receiverId: Long = 0,

    @Column(nullable = false)
    var fileName: String = "",

    @Column(nullable = false)
    var contentType: String = "audio/mp4",

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)