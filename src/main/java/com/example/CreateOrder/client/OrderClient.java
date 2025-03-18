package com.example.CreateOrder.client;

import com.example.CreateOrder.exceptions.ProductNotFoundException;
import com.example.CreateOrder.models.ProductRequest;
import com.example.CreateOrder.models.ProductResponse;
import com.example.CreateOrder.models.ProductServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OrderClient {

    private ManagedChannel channel;
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    @PostConstruct
    private void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext() 
                .build();

        productServiceBlockingStub = ProductServiceGrpc.newBlockingStub(channel);
    }
    public ProductResponse getProductById(String productId) {
        System.out.println("gRPC client requesting product details for ID: " + productId);
        ProductRequest request = ProductRequest.newBuilder()
                .setProductId(productId)
                .build();
        try {
            System.out.println("gRPC: Received product details from ProductService for product ID: " + productId);
            return productServiceBlockingStub.getProductById(request);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new ProductNotFoundException("Product not found with ID: " + productId);
            } else {
                throw new RuntimeException("Failed to retrieve product: " + e.getMessage(), e);
            }
        }
    }



}