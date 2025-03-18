package com.example.CreateOrder.controllers;

import com.example.CreateOrder.echanges.KafkaEventPublisher;
import com.example.CreateOrder.models.OrderEvent;
import com.example.CreateOrder.models.OrderEvent.OrderState;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    private final KafkaEventPublisher eventPublisher;

    public TestController(KafkaEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/send")
    public String sendMessage() {
        OrderEvent testEvent = new OrderEvent();
        testEvent.setOrderId("test-order-123");
        testEvent.setProductId("test-product-456");
        testEvent.setQuantity(2);
        testEvent.setState(OrderState.CREATED);
        
        eventPublisher.publishOrderEvent(testEvent);
        return "Test message sent to Kafka!";
    }
}
