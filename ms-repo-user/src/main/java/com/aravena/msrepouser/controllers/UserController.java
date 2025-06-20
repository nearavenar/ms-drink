package com.aravena.msrepouser.controllers;

import com.aravena.msrepouser.models.User;
import com.aravena.msrepouser.services.UserService;
import com.lowagie.text.DocumentException;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("repo")
//@AllArgsConstructor
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/create-pdf/{id}")
    public ResponseEntity<byte[]> createReport(@PathVariable Long id) throws DocumentException, IOException {
        byte[] repo = userService.createReport(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "report_"+id+".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(repo);
    }

    @GetMapping("/send-email/{id}")
    public String sendEmailNewPassword(@PathVariable Long id) throws MessagingException {
            userService.sendEmailNewPassword(id);
            return "email sender success";
    }
}
