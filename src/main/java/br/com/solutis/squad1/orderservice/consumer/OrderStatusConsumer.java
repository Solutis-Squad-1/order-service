package br.com.solutis.squad1.orderservice.consumer;

import br.com.solutis.squad1.orderservice.dto.order.UpdateOrderStatusDto;
import br.com.solutis.squad1.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusConsumer {
    private final OrderService orderService;

    @Value("${spring.rabbitmq.queue.order.status}")
    private String orderStatusQueueName;

    @RabbitListener(queues = {"spring.rabbitmq.queue.order.status"})
    public void consume(
            @Payload UpdateOrderStatusDto updateOrderStatusDto
            ) {
        log.info("Consuming message from queue: {}", orderStatusQueueName);
        log.info("Message: {}", updateOrderStatusDto);
        orderService.updateStatus(updateOrderStatusDto);
    }
}
