package dev.rm.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import dev.rm.model.Order;
import dev.rm.model.OrderItem;
import dev.rm.model.OrderRequest;
import dev.rm.model.Product;
import dev.rm.model.User;
import dev.rm.repository.OrderItemRepository;
import dev.rm.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Product product;
    private OrderRequest.ProductRequest productRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .username("Test User")
                .email("test@example.com").build();

        product = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(new BigDecimal("100.00")).build();

        productRequest = new OrderRequest.ProductRequest();
        productRequest.setProductId(product.getId());
        productRequest.setQuantity(2);
    }

    @Test
    public void testCreateOrder_Success() {

        Long userId = 1L;
        List<OrderRequest.ProductRequest> productRequests = Arrays.asList(productRequest);

        when(userService.getUserById(userId)).thenReturn(user);
        when(productService.getProductsByIds(Arrays.asList(product.getId())))
                .thenReturn(Arrays.asList(product));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());
        when(orderItemRepository.saveAll(anyList())).thenReturn(Arrays.asList(new OrderItem()));

        Order order = orderService.createOrder(user.getId(), productRequests);

        assertNotNull(order);
        assertEquals(1, order.getItems().size());
        assertEquals(new BigDecimal("200.00"), order.getTotalAmount());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testCreateOrder_UserNotFound() {

        Long userId = 999L;
        List<OrderRequest.ProductRequest> productRequests = Arrays.asList(productRequest);

        when(userService.getUserById(userId)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(userId, productRequests);
        });

        assertEquals("User with ID " + userId + " not found", exception.getMessage());
    }

    @Test
    public void testCreateOrder_ProductNotFound() {

        Long userId = 1L;
        List<OrderRequest.ProductRequest> productRequests = Arrays.asList(productRequest);

        when(userService.getUserById(userId)).thenReturn(user);
        when(productService.getProductsByIds(Arrays.asList(product.getId()))).thenReturn(Arrays.asList());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(userId, productRequests);
        });

        assertEquals("Product with ID 1 not found", exception.getMessage());
    }

    @Test
    public void testGetOrdersByUserId() {

        Long userId = 1L;
        Order mockOrder = new Order();
        mockOrder.setUserId(userId);

        when(orderRepository.findByUserId(userId)).thenReturn(Arrays.asList(mockOrder));

        List<Order> orders = orderService.getOrdersByUserId(userId);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(userId, orders.get(0).getUserId());
    }
}
