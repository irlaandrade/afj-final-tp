package puc.dlq

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class DeadLetterQueueListener {

    @RabbitListener(queues = ["dead-letter-queue"])
    fun listen(message: String) {
        println("Received message in DLQ: $message")
        // adicionar a lógica para processar a mensagem da DLQ
    }
}