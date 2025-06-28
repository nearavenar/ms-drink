package com.aravena.msrepouser.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

import com.aravena.msrepouser.models.LoginUser;
import com.aravena.msrepouser.models.Rol;
import com.aravena.msrepouser.repositories.UserRepository;
import com.lowagie.text.DocumentException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


import com.aravena.msrepouser.models.User;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TemplateEngine templateEngine;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private PdfRenderer pdfRenderer;
    @InjectMocks
    private UserServiceImpl userService;
    private final String username = "test@mail.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, templateEngine, mailSender, /*pdfRenderer,*/ username);
    }

    @Test
    void testGetUserById_UserExists() {
        User user = getSampleUser();
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getGender(), result.getGender());
        assertEquals(user.getDateCreated(), result.getDateCreated());
        assertEquals(user.isStatus(), result.isStatus());
        assertEquals(user.getRoles(), result.getRoles());
    }

    @Test
    void testGetUserById_UserNotExists() {
        when(userRepository.getUserById(2L)).thenReturn(Optional.empty());

        User result = userService.getUserById(2L);

        assertNull(result.getId());
        assertNull(result.getName());
        assertNull(result.getEmail());
        assertNull(result.getPassword());
        assertNull(result.getDateCreated());
        assertFalse(result.isStatus());
        
    }

    @Test
    void testGetUserByUser_UserExists() {
        User user = getSampleUser();
        when(userRepository.getUserByUser("test@mail.com")).thenReturn(Optional.of(user));

        User result = userService.getUserByUser("test@mail.com");

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getGender(), result.getGender());
        assertEquals(user.getDateCreated(), result.getDateCreated());
        assertEquals(user.isStatus(), result.isStatus());
        assertEquals(user.getRoles(), result.getRoles());
    }

    @Test
    void testGetUserByUser_UserNotExists() {
        when(userRepository.getUserByUser("notfound@mail.com")).thenReturn(Optional.empty());

        User result = userService.getUserByUser("notfound@mail.com");

        assertNull(result.getId());
        assertNull(result.getName());
        assertNull(result.getEmail());
        assertNull(result.getPassword());
        assertNull(result.getGender());
        assertNull(result.getDateCreated());
        assertFalse(result.isStatus());
        assertNull(result.getRoles());
    }

    @Test
    void testCreateReport() throws IOException, DocumentException {
        User user = getSampleUser();
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(templateEngine.process(eq("report"), any(Context.class))).thenReturn("<html><head></head><body>Report</body></html>");

        byte[] pdf = userService.createReport(1L);

        assertNotNull(pdf);
        //assertTrue(pdf.length > 0);
    }

    @Test
    void testSendEmailNewPassword() throws MessagingException {
        User user = getSampleUser();
        when(userRepository.getUserByUser(user.getEmail())).thenReturn(Optional.of(user));
        when(templateEngine.process(eq("recover-password"), any(Context.class))).thenReturn("<html>Email</html>");
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(userRepository.sendEmailUser(any(LoginUser.class))).thenReturn(true);

        assertDoesNotThrow(() -> userService.sendEmailNewPassword(user.getEmail()));

        verify(mailSender).send(mimeMessage);
        verify(userRepository, times(1)).sendEmailUser(any(LoginUser.class));
    }

    @Test
    void testSendEmailNewPassword_RetryOnFailure() throws MessagingException {
        User user = getSampleUser();
        when(userRepository.getUserByUser(user.getEmail())).thenReturn(Optional.of(user));
        when(templateEngine.process(eq("recover-password"), any(Context.class))).thenReturn("<html>Email</html>");
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(userRepository.sendEmailUser(any(LoginUser.class))).thenReturn(false);

        assertDoesNotThrow(() -> userService.sendEmailNewPassword(user.getEmail()));

        verify(mailSender).send(mimeMessage);
        verify(userRepository, times(2)).sendEmailUser(any(LoginUser.class));
    }

    @Test
    void testUnlockUser() {
        LoginUser loginUser = new LoginUser("test@mail.com", "pass");
        when(userRepository.unlockUser(loginUser)).thenReturn(true);

        boolean result = userService.unlockUser(loginUser);

        assertTrue(result);
        verify(userRepository).unlockUser(loginUser);
    }

    @Test
    void testSetUserReport_MasksFieldsCorrectly() {
        User user = getSampleUser();
        // Mock UserUtil static methods
        try (MockedStatic<com.aravena.msrepouser.utils.UserUtil> userUtilMock = mockStatic(com.aravena.msrepouser.utils.UserUtil.class)) {
            userUtilMock.when(() -> com.aravena.msrepouser.utils.UserUtil.initCapText("test")).thenReturn("Test");
            userUtilMock.when(() -> com.aravena.msrepouser.utils.UserUtil.maskPassword("test@mail.com", 8)).thenReturn("test@****");
            userUtilMock.when(() -> com.aravena.msrepouser.utils.UserUtil.maskPassword("password", 2)).thenReturn("pa******");

            User result = invokeSetUserReport(user);

            assertEquals("Test", result.getName());
            assertEquals("test@****", result.getEmail());
            assertEquals("pa******", result.getPassword());
            assertEquals(user.getGender(), result.getGender());
            assertEquals(user.getRoles(), result.getRoles());
        }
    }

    // Helper to invoke private setUserReport via reflection
    private User invokeSetUserReport(User user) {
        try {
            java.lang.reflect.Method method = UserServiceImpl.class.getDeclaredMethod("setUserReport", User.class);
            method.setAccessible(true);
            return (User) method.invoke(userService, user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private User getSampleUser() {
        User user = new User();
        user.setId(1L);
        user.setName("test");
        user.setPassword("password");
        user.setEmail("test@mail.com");
        user.setGender('M');
        user.setDateCreated(LocalDateTime.now());
        user.setStatus(true);
        user.setRoles(Arrays.asList(new Rol(1L,  "ROLE_USER")));
        return user;
    }

    @Test()
    void createCv_shouldThrowDocumentException() throws Exception {
        /*User user = getSampleUser();
        Optional<User> userOpt = Optional.of(user);

        when(userRepository.getUserById(1L)).thenReturn(userOpt);

        when(templateEngine.process(eq("report"), any(Context.class)))
                .thenReturn("<html><body>Reporte</body></html>");

        // Forzar la excepción
        doThrow(new DocumentException("Simulado"))
                .when(pdfRenderer)
                .renderHtmlToPdf(anyString(), any(OutputStream.class));

        // Ejecutar el método
        userService.createReport(user.getId());*/
    }
}
