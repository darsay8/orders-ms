package dev.rm.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private Long userId;
    private List<ProductRequest> products;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductRequest {
        private Long productId;
        private Integer quantity;
    }
}