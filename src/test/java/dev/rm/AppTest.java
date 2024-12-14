package dev.rm;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.rm.service.OrderService;

@SpringBootTest
class AppTest {

    @Autowired
    private OrderService orderService;

    @Test
    void contextLoads() {
        assertNotNull(orderService, "OrderService should be loaded into the application context");
    }

    @Test
    void main() {
        App.main(new String[] {});
    }
}
