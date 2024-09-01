package puc.dlq

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class PurchaseDeadLetterListener {

    @RabbitListener(queues = ["dead-letter-queue"], containerFactory = "purchaseDeadLetterListenerContainerFactory")
    fun listen(message: String) {
        println("Received message: $message")
        // Simular um erro lançando uma exceção
        if (message.contains("error")) {
            throw RuntimeException("Simulated error processing message: $message")
        }
    }
}