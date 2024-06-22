package com.ankush.cleancity.MailStuff;

import com.ankush.cleancity.Wastes.EmailedWaste;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailBean {
    @Autowired
    private JavaMailSender emailSender;


    public void notifyComplaint(EmailedWaste w) {
        String msg = String.format("Complaint with id %d complete", w.getWaste().getId());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cleancityconnect@healthierme.in");
        message.setTo(w.getEmail());
        message.setSubject("Complaint Acknowledged");
        message.setText(msg);
        new Thread(new RunnableSendSimpleMail(message, emailSender)).start();
    }

}
