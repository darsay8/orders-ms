package dev.rm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dev.rm.model.User;

@Service
public class UserService {

    @Value("${service.user.url}")
    private String userServiceUrl;

    private final RestTemplate restTemplate;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public User getUserById(Long userId) {
        String url = userServiceUrl + "/" + userId;
        return restTemplate.getForObject(url, User.class);
    }
}
