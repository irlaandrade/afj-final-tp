package puc.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import puc.controller.PurchaseRequest
import puc.model.PurchaseMessage

@Service
class PurchaseService {
    @Autowired
    private var rabbitTemplate: RabbitTemplate? = null

    @Value("\${rabbitmq.exchange}")
    private val exchange: String? = null

    @Value("\${rabbitmq.routingkey}")
    private val routingKey: String? = null

    fun PurchaseService(rabbitTemplate: RabbitTemplate?) {
        this.rabbitTemplate = rabbitTemplate
    }

    @Throws(JsonProcessingException::class)
    fun sendMessage(userId: Long, purchaseRequest: PurchaseRequest) {
        val purchaseMessage = PurchaseMessage(userId, purchaseRequest.productId, purchaseRequest.quantity)
        val objectMapper = ObjectMapper()
        val messageAsString: String = objectMapper.writeValueAsString(purchaseMessage)
        rabbitTemplate!!.convertAndSend(exchange, routingKey, messageAsString)
    }
}