
package com.example.flowterserver

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {

    @GetMapping("/")
    fun home(): Map<String, String> {
        return mapOf(
            "application" to "Flowter",
            "status" to "online",
            "message" to "Le serveur Flowter fonctionne correctement."
        )
    }
}
