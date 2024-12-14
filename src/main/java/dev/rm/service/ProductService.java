package dev.rm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dev.rm.model.Product;

@Service
public class ProductService {

    private final RestTemplate restTemplate;

    @Value("${service.product.url}")
    private String productServiceUrl;

    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Product> getProductsByIds(List<Long> productIds) {
        String url = String.format("%s/by-ids", productServiceUrl);

        ResponseEntity<List<Product>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(productIds),
                new ParameterizedTypeReference<List<Product>>() {
                });

        return response.getBody();
    }
}
