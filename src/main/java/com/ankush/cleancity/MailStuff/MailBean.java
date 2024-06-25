package com.ankush.cleancity.MailStuff;

import com.ankush.cleancity.Wastes.EmailedWaste;
import com.ankush.cleancity.Wastes.Waste;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@Slf4j
public class MailBean {
    @Autowired
    private JavaMailSender emailSender;


    public void sendSimpleEmail(String email, String subject, String text, Waste w) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cleancityconnect@healthierme.in");
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        new Thread(new RunnableSendSimpleMail(message, emailSender)).start();
//        log.info("EMAIL SENT TO {} , {}", email,text);
    }


    public void notifyComplaint(EmailedWaste w) {
        String msg = String.format("Complaint with id %d complete", w.getWaste().getId());
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("cleancityconnect@healthierme.in");
//        message.setTo(w.getEmail());
//        message.setSubject("Complaint Acknowledged");
//        message.setText(msg);
//        new Thread(new RunnableSendSimpleMail(message, emailSender)).start();
        sendSimpleEmail(w.getEmail(), "Complaint Acknowledged", msg, w.getWaste());
    }

    public void notifyInvalidPerson(EmailedWaste w) {
        sendSimpleEmail(w.getEmail(), "Invalid person", "invalidated", w.getWaste());
    }

    public void notifyInvalidAI(EmailedWaste w) {
        sendSimpleEmail(w.getEmail(), "Invalid AI", "invalidated", w.getWaste());

    }

    public void complaintRaised(String manager, Waste w) {
        log.info("MANAGER MAILED {}", manager);
        sendSimpleEmail(manager, "Complaint Raised", "manager", w);
    }


}
