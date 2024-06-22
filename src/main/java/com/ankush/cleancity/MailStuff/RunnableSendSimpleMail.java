package com.ankush.cleancity.MailStuff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;

@AllArgsConstructor
@Getter
@Setter
@Slf4j
public class RunnableSendSimpleMail implements Runnable {
    private SimpleMailMessage message;
    private JavaMailSender mailSender;

    @Override
    public void run() {
        try {
            mailSender.send(message);
            log.info("SENT MAIL TO:{}", Arrays.toString(message.getTo()));
        } catch (Exception e) {
            log.error("ERROR SENDING MAIL", e);
        }

    }
}
