package dev.rm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.rm.model.Order;
import dev.rm.model.OrderRequest;
import dev.rm.model.User;
import dev.rm.service.OrderService;
import dev.rm.service.UserService;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    @InjectMocks
    private OrderController orderController;

    private ObjectMapper objectMapper;

    User user;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();

        user = User.builder()
                .id(1L)
                .username("test user")
                .email("test@example.com").build();
    }

    @Test
    public void testCreateOrder() throws Exception {

        OrderRequest.ProductRequest productRequest = new OrderRequest.ProductRequest(1L, 2);
        List<OrderRequest.ProductRequest> productRequests = Arrays.asList(productRequest);
        Order order = Order.builder().userId(user.getId()).build();

        when(orderService.createOrder(user.getId(), productRequests)).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new OrderRequest(user.getId(), productRequests))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(user.getId()));
    }

    @Test
    public void testGetOrdersByUser() throws Exception {

        Long userId = 1L;
        List<Order> orders = Arrays.asList(new Order(), new Order());
        when(orderService.getOrdersByUserId(userId)).thenReturn(orders);

        mockMvc.perform(get("/api/orders/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists());
    }

    @Test
    public void testGetUserById() throws Exception {

        when(userService.getUserById(user.getId())).thenReturn(user);

        mockMvc.perform(get("/api/orders/user/id/{userId}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    // @Test
    // public void testCreateOrder_UserNotFound() throws Exception {

    // OrderRequest orderRequest = new OrderRequest(user.getId(),
    // Arrays.asList(new OrderRequest.ProductRequest(1L, 2)));

    // when(orderService.createOrder(user.getId(), orderRequest.getProducts()))
    // .thenThrow(new IllegalArgumentException("User not found"));

    // mockMvc.perform(post("/api/orders")
    // .contentType(MediaType.APPLICATION_JSON)
    // .content(objectMapper.writeValueAsString(orderRequest)))
    // .andExpect(status().isBadRequest())
    // .andExpect(jsonPath("$.message").value("User not found"));
    // }
}
