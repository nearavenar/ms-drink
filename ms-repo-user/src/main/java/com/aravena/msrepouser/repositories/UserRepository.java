package com.aravena.msrepouser.repositories;

import com.aravena.msrepouser.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
@FeignClient(name = "ms-users")
public interface UserRepository {
    @GetMapping("ms-users/users/{id}")
    Optional<User> getUserById(@PathVariable Long id);
}
