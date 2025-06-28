package com.aravena.msrepouser.services;

import com.aravena.msrepouser.models.LoginUser;
import com.aravena.msrepouser.models.User;
import com.lowagie.text.DocumentException;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface UserService {
    User getUserById(Long id);
    User getUserByUser(String email);
    byte[] createReport(Long id) throws IOException, DocumentException;
    void sendEmailNewPassword(String email) throws MessagingException;
    boolean unlockUser(LoginUser login);

    byte[] createCv() throws IOException, DocumentException;
}
