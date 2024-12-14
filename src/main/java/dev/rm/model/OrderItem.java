package dev.rm.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_item")
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "id_order", nullable = false)
    @JsonBackReference
    private Order order;

    @Column(name = "id_product", nullable = false)
    private Long productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    public void calculateTotalPrice(BigDecimal price) {
        this.unitPrice = price;
        this.totalPrice = price.multiply(new BigDecimal(quantity));
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }
}
