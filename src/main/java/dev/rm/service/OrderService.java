package dev.rm.service;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import dev.rm.repository.OrderRepository;
import dev.rm.repository.OrderItemRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.rm.model.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;
    private final UserService userService;

    public OrderService(OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductService productService,
            UserService userService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
        this.userService = userService;
    }

    @Transactional
    public Order createOrder(Long userId, List<OrderRequest.ProductRequest> productRequests) {

        User user = userService.getUserById(userId);

        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }

        List<Long> productIds = productRequests.stream()
                .map(OrderRequest.ProductRequest::getProductId)
                .collect(Collectors.toList()); // Convert to a list of IDs
        List<Product> products = productService.getProductsByIds(productIds);

        Order order = new Order();
        order.setUserId(user.getId());

        order.setItems(new ArrayList<>());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (int i = 0; i < productRequests.size(); i++) {
            Product product = products.get(i);
            OrderRequest.ProductRequest productRequest = productRequests.get(i);

            Integer quantity = productRequest.getQuantity();

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setQuantity(quantity);
            orderItem.setOrder(order);
            orderItem.calculateTotalPrice(product.getPrice());

            totalAmount = totalAmount.add(orderItem.getTotalPrice());
            order.getItems().add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        orderRepository.save(order);
        orderItemRepository.saveAll(order.getItems());

        return order;
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
