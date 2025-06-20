package com.aravena.msrepouser.services;

import com.aravena.msrepouser.models.User;
import com.lowagie.text.DocumentException;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface UserService {

    User getUserById(Long id);
    byte[] createReport(Long id) throws IOException, DocumentException;
    void sendEmailNewPassword(Long id) throws MessagingException;
}
