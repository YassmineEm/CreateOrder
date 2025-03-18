package com.example.CreateOrder.echanges;

import com.example.CreateOrder.config.KafkaConfig;
import com.example.CreateOrder.models.OrderEvent;
import com.example.CreateOrder.models.ProductEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderEvent(OrderEvent event) {
        if (event.getState() == null) {
            event.setState(OrderEvent.OrderState.CREATED); 
        }
        kafkaTemplate.send(KafkaConfig.ORDER_TOPIC, event.getOrderId(), event);
        System.out.println("Published Order Event to Kafka: " + event);
    }

    public void publishProductEvent(ProductEvent event) {
        kafkaTemplate.send(KafkaConfig.PRODUCT_TOPIC, event.getProductId(), event);
        System.out.println("Published Product Event to Kafka: " + event);
    }
}