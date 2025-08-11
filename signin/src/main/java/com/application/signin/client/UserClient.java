package com.application.signin.client;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.application.signin.entity.User;

@FeignClient(name = "signup-service", url = "http://localhost:8084")
public interface UserClient {
    @GetMapping("/signup/user/email/{email}")
    User getUserByEmail(@PathVariable("email") String email);
}
