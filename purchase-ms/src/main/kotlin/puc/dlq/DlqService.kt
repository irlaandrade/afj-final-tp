package puc.dlq

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class DlqService(val rabbitTemplate: RabbitTemplate) {

    fun sendDlqMessage(message: String) {
        rabbitTemplate.convertAndSend("dead-letter-routing-key", message)
    }
}