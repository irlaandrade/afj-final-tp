package puc.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder
import org.springframework.retry.interceptor.RetryOperationsInterceptor

@Configuration
class RabbitMQConfig {

    @Value("\${rabbitmq.exchange}")
    lateinit var exchange: String

    @Value("\${rabbitmq.routingkey}")
    lateinit var routingKey: String

    @Value("\${rabbitmq.queue}")
    lateinit var purchaseQueue: String

    @Bean
    fun purchaseQueue(): Queue {
        return Queue(purchaseQueue, true)
    }

    @Bean
    fun exchange(): org.springframework.amqp.core.Exchange {
        return ExchangeBuilder.directExchange(exchange).durable(true).build()
    }

    @Bean
    fun rabbitAdmin(connectionFactory: ConnectionFactory): RabbitAdmin {
        return RabbitAdmin(connectionFactory)
    }

    @Bean
    fun simpleRabbitListenerContainerFactory(connectionFactory: ConnectionFactory): SimpleRabbitListenerContainerFactory {
        val retryTemplate = RetryTemplate()
        val retryPolicy = SimpleRetryPolicy(3)
        retryTemplate.setRetryPolicy(retryPolicy)

        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setConcurrentConsumers(3)
        factory.setMaxConcurrentConsumers(10)
        factory.setPrefetchCount(1)
        factory.setRetryTemplate(retryTemplate)
        return factory
    }

    @Bean
    fun binding(): org.springframework.amqp.core.Binding {
        return org.springframework.amqp.core.BindingBuilder
            .bind(purchaseQueue())
            .to(exchange())
            .with(routingKey)
            .noargs()
    }

    @Bean
    fun deadLetterQueue(): Queue {
        return Queue("dead-letter-queue", true, false, false)
    }

    @Bean
    fun deadLetterExchange(): DirectExchange {
        return DirectExchange("dead-letter-exchange")
    }

    @Bean
    fun dlxBinding(): Binding {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("dead-letter-routing-key")
    }

    @Bean
    fun purchaseDeadLetterListenerContainerFactory(connectionFactory: ConnectionFactory): SimpleRabbitListenerContainerFactory {
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)

        val retryInterceptor: RetryOperationsInterceptor = RetryInterceptorBuilder.stateless()
            .maxAttempts(3)
            .recoverer(RejectAndDontRequeueRecoverer())
            .build()

        factory.setAdviceChain(retryInterceptor)

        return factory
    }
}