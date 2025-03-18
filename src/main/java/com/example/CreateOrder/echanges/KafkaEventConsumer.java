package com.example.CreateOrder.echanges;

import com.example.CreateOrder.config.KafkaConfig;
import com.example.CreateOrder.models.Order;
import com.example.CreateOrder.models.OrderEvent;
import com.example.CreateOrder.models.Product;
import com.example.CreateOrder.models.ProductEvent;
import com.example.CreateOrder.repositories.OrderRepository;
import com.example.CreateOrder.repositories.ProductRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaEventConsumer {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final KafkaEventPublisher eventPublisher;

    public KafkaEventConsumer(ProductRepository productRepository,
                            OrderRepository orderRepository,
                            KafkaEventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @KafkaListener(topics = KafkaConfig.ORDER_TOPIC, groupId = "order-group")
    public void handleOrderEvent(OrderEvent orderEvent) {
        System.out.println("Step 5: [Kafka] Received order event - Order ID: " + 
                orderEvent.getOrderId() + ", Product ID: " + orderEvent.getProductId());
        
        String productId = orderEvent.getProductId();
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            int requestedQuantity = orderEvent.getQuantity();

            ProductEvent productEvent = new ProductEvent();
            productEvent.setProductId(productId);
            productEvent.setOrderId(orderEvent.getOrderId());

            if (product.getQuantity() >= requestedQuantity) {
                System.out.println("Product available. Updating quantity for: " + productId);
                product.setQuantity(product.getQuantity() - requestedQuantity);
                productRepository.save(product);
                productEvent.setState(ProductEvent.ProductState.AVAILABLE);
            } else {
                System.out.println("Product out of stock: " + productId);
                productEvent.setState(ProductEvent.ProductState.OUT_OF_STOCK);
            }

            eventPublisher.publishProductEvent(productEvent);
        }
    }

    @KafkaListener(topics = KafkaConfig.PRODUCT_TOPIC, groupId = "product-group")
    public void handleProductEvent(ProductEvent productEvent) {
        System.out.println("[Kafka] Received product event - Order ID: " + 
                productEvent.getOrderId() + ", State: " + productEvent.getState());

        String orderId = productEvent.getOrderId();
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            switch (productEvent.getState()) {
                case AVAILABLE:
                    System.out.println("Updating order " + orderId + " to PROCESSING");
                    order.setState(Order.OrderState.PROCESSING);
                    break;
                case OUT_OF_STOCK:
                    System.out.println("Updating order " + orderId + " to FAILED");
                    order.setState(Order.OrderState.FAILED);
                    break;
            }

            orderRepository.save(order);
            System.out.println("Order " + orderId + " updated successfully");
        }
    }
}