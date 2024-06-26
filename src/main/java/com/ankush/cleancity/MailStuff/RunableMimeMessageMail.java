package com.ankush.cleancity.MailStuff;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;

@AllArgsConstructor
@Slf4j
public class RunableMimeMessageMail implements Runnable {
    private MimeMessage message;
    private JavaMailSender mailSender;

    @Override
    public void run() {
        try {
            log.info("SENDING MAIL TO:{}", Arrays.toString(message.getAllRecipients()));
            mailSender.send(message);
            log.info("SENT MAIL TO:{}", Arrays.toString(message.getAllRecipients()));
        } catch (Exception e) {
            log.error("ERROR SENDING MAIL", e);
        }

    }

}
