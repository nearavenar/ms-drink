package com.aravena.msrepouser.services;

import com.aravena.msrepouser.models.LoginUser;
import com.aravena.msrepouser.models.User;
import com.aravena.msrepouser.repositories.UserRepository;
import com.aravena.msrepouser.utils.PasswordGenerator;
import com.aravena.msrepouser.utils.UserUtil;
import com.lowagie.text.DocumentException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final String username;

    public UserServiceImpl(UserRepository userRepository,
                           TemplateEngine templateEngine,
                           JavaMailSender mailSender,
                           @Value("${spring.mail.username}") String username) {
        this.userRepository = userRepository;
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
        this.username = username;
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> getUser = userRepository.getUserById(id);
        User user = new User();
        if(getUser.isPresent()){
            user.setId(getUser.get().getId());
            user.setName(getUser.get().getName());
            user.setPassword(getUser.get().getPassword());
            user.setEmail(getUser.get().getEmail());
            user.setGender(getUser.get().getGender());
            user.setDateCreated(getUser.get().getDateCreated());
            user.setStatus(getUser.get().isStatus());
            user.setRoles(getUser.get().getRoles());
        }
        return user;
    }

    @Override
    public User getUserByUser(String email) {
        Optional<User> getUser = userRepository.getUserByUser(email);
        User user = new User();
        if(getUser.isPresent()){
            user.setId(getUser.get().getId());
            user.setName(getUser.get().getName());
            user.setPassword(getUser.get().getPassword());
            user.setEmail(getUser.get().getEmail());
            user.setGender(getUser.get().getGender());
            user.setDateCreated(getUser.get().getDateCreated());
            user.setStatus(getUser.get().isStatus());
            user.setRoles(getUser.get().getRoles());
        }
        return user;
    }

    @Override
    public byte[] createReport(Long id) throws IOException, DocumentException {

        User user = getUserById(id);

        Context context = new Context();
        context.setVariable("title", "Description user " + user.getId());
        context.setVariable("photo", UserUtil.getImage(user.getGender()));
        context.setVariable("date", LocalDate.now());
        context.setVariable("dateCreated", UserUtil.getDateTime(user.getDateCreated()));
        context.setVariable("user", setUserReport(user));

        String html = templateEngine.process("report", context);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        }
    }

    private User setUserReport(User user) {
        user.setName(UserUtil.initCapText(user.getName()));
        user.setEmail(UserUtil.maskPassword(user.getEmail(), 8));
        user.setPassword(UserUtil.maskPassword(user.getPassword(), 2));
        user.setGender(user.getGender());
        user.setRoles(user.getRoles());
        return user;
    }

    @Override
    public void sendEmailNewPassword(String email) throws MessagingException {

        User user = getUserByUser(email);
        String password = PasswordGenerator.generateRandomPassword();

        Context context = new Context();
        context.setVariable("name", user.getName());
        context.setVariable("date", UserUtil.getDateTime(LocalDateTime.now()));
        context.setVariable("password", password);

        String htmlContent = templateEngine.process("recover-password", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        helper.setTo(user.getEmail());
        helper.setSubject("Temporal password by access");
        helper.setText(htmlContent, true);
        helper.setFrom(username);

        ClassPathResource image = new ClassPathResource("static/images/logo-pizza.jpg");
        helper.addInline("footerImage", image);

        mailSender.send(message);

        LoginUser login = new LoginUser(email, password);
        boolean send = userRepository.sendEmailUser(login);
        if (!send) {
            userRepository.sendEmailUser(login);
        }
    }

    @Override
    public boolean unlockUser(LoginUser login) {
        return userRepository.unlockUser(login);
    }
}
