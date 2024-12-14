package dev.rm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.rm.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
