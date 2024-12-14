package dev.rm.model;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {

    private Long userId;
    private List<ProductRequest> products;

    @Data
    public static class ProductRequest {
        private Long productId;
        private Integer quantity;
    }
}