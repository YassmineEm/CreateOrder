package com.example.CreateOrder.models;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private String id; 

    private Double price;
    private String productId;
    private int quantity;

    @Enumerated(EnumType.STRING) 
    private OrderState state; 

   
    public enum OrderState {
        CREATED,
        PROCESSING,
        FAILED
    }
}