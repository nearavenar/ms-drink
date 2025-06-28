package com.aravena.msrepouser.controllers;

import com.aravena.msrepouser.models.LoginUser;
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
        //byte[] repo = userService.createCv();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "report_"+id+".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(repo);
    }

    @GetMapping("/send-email/{email}")
    public String sendEmailNewPassword(@PathVariable String email) throws MessagingException {
            userService.sendEmailNewPassword(email);
            return "email sender success";
    }

    @PostMapping("/unlock-user")
    public String unlockUser(@RequestBody LoginUser loginUser) throws MessagingException {
        userService.unlockUser(loginUser);
        return "unlock user success";
    }
}
