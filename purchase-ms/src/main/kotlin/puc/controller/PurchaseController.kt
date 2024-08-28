package puc.controller

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import puc.service.PurchaseService
import puc.util.JwtUtil


@RestController
@RequestMapping("/purchase")
class PurchaseController(val rabbitTemplate: RabbitTemplate, val jwtUtil: JwtUtil, val purchaseService: PurchaseService) {

    @Value("\${rabbitmq.exchange}")
    lateinit var exchange: String

    @Value("\${rabbitmq.routingkey}")
    lateinit var routingKey: String

    @PostMapping("/buy")
    fun buy(@RequestHeader("Authorization") token: String, @RequestBody purchaseRequest: PurchaseRequest): ResponseEntity<String> {
        val bearer = token.replaceFirst("Bearer ", "")
        if (!JwtUtil.validateToken(bearer)) {
            return ResponseEntity.status(401).body("Failed to authenticate.")
        }

        try {
            val userId: Long = JwtUtil.extractUserId(bearer)
            purchaseService.sendMessage(userId, purchaseRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.status(500).body("Failed to send purchase request.")
        }
        return ResponseEntity.ok("Purchase request sent.")
    }
}

data class PurchaseRequest(val productId: String, val quantity: Int)