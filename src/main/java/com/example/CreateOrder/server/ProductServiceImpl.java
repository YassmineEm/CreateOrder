package com.example.CreateOrder.server;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import com.example.CreateOrder.models.Product;
import com.example.CreateOrder.models.ProductRequest;
import com.example.CreateOrder.models.ProductResponse;
import com.example.CreateOrder.models.ProductServiceGrpc;
import com.example.CreateOrder.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@GrpcService
public class ProductServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void getProductById(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        String productId = request.getProductId();
        System.out.println("gRPC Server responding to Step 2: Product details request for ID: " + productId);

        Optional<Product> productOptional = productRepository.findById(productId);
        System.out.println("Product ID received: '" + productId + "'");

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            ProductResponse response = ProductResponse.newBuilder()
                    .setProductId(product.getId())
                    .setName(product.getName())
                    .setDescription(product.getDescription())
                    .setPrice(product.getPrice())
                    .setQuantity(product.getQuantity())
                    .build();

            System.out.println("gRPC Server: Sending product details for product ID: " + productId);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            System.out.println("gRPC Server: Product not found with ID: " + productId);
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Product not found with ID: " + productId)
                    .asRuntimeException());}

    }
}