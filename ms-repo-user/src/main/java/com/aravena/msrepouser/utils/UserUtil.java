package com.aravena.msrepouser.utils;

import jakarta.ws.rs.NotFoundException;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URL;
import java.text.BreakIterator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UserUtil {
    public static String maskPassword(String password, int lengthValue) {
        if (password == null) {
            return "";
        }

        if (password.length() <= lengthValue) {
            return "*".repeat(password.length());
        }

        int middleLength = password.length() - lengthValue;
        int show = lengthValue/2;
        return password.substring(0, show) + "*".repeat(middleLength) + password.substring(password.length() - show);
    }

    public static String initCapText(String text) {
        if (text == null || text.isEmpty()) return text;

        BreakIterator wordIterator = BreakIterator.getWordInstance(new Locale("es", "ES"));
        wordIterator.setText(text.toLowerCase(new Locale("es", "ES")));

        StringBuilder result = new StringBuilder();
        int start = wordIterator.first();

        for (int end = wordIterator.next(); end != BreakIterator.DONE; start = end, end = wordIterator.next()) {
            String word = text.substring(start, end);
            if (Character.isLetterOrDigit(word.charAt(0))) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1));
            } else {
                result.append(word);
            }
        }

        return result.toString();
    }

    public static String getImage(char gender) throws IOException {
        URL resource;
        if(gender == 'M'){
            resource = new ClassPathResource("static/images/man-person.png").getURL();
        }else{
            resource = new ClassPathResource("static/images/woman-person.png").getURL();
        }
        return resource.toExternalForm();
    }

    public static String getDateTime(LocalDateTime dateNow){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return dateNow.format(formatter);
    }
}
