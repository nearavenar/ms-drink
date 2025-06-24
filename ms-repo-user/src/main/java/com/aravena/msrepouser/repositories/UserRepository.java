package com.aravena.msrepouser.repositories;

import com.aravena.msrepouser.models.LoginUser;
import com.aravena.msrepouser.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "ms-users")
public interface UserRepository {
    @GetMapping("ms-users/users/find-by-id/{id}")
    Optional<User> getUserById(@PathVariable Long id);
    @GetMapping("ms-users/users/find-by-user/{email}")
    Optional<User> getUserByUser(@PathVariable String email);
    @PostMapping("ms-users/users/unlock-user")
    boolean unlockUser(@RequestBody LoginUser login);
    @PostMapping("ms-users/users/send-email-user")
    boolean sendEmailUser(@RequestBody LoginUser login);
}
