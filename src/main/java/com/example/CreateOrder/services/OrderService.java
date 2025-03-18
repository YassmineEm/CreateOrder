package com.example.CreateOrder.services;


import com.example.CreateOrder.exceptions.ProductNotFoundException;
import com.example.CreateOrder.echanges.KafkaEventPublisher;
import com.example.CreateOrder.models.Order;
import com.example.CreateOrder.models.OrderEvent;
import com.example.CreateOrder.models.Product;
import com.example.CreateOrder.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaEventPublisher eventPublisher;

    public Page<Order> allOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Optional<Order> singleOrder(String id){
        return Optional.ofNullable(orderRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Order not found with ID: " + id)));
    }

    public Order createAndProcessOrder(Product product, Order order) {
        order.setPrice(order.getQuantity() * product.getPrice());

        order.setState(Order.OrderState.CREATED);

        
        System.out.println("Step 3: Saving new order with CREATED state");
        Order savedOrder = orderRepository.save(order);

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(savedOrder.getId());
        orderEvent.setProductId(savedOrder.getProductId());
        orderEvent.setQuantity(savedOrder.getQuantity());

        eventPublisher.publishOrderEvent(orderEvent);

        return savedOrder;
    }

    private void validateOrder(Product product, Order order) {
        order.setPrice(order.getQuantity() * product.getPrice());
    }
}