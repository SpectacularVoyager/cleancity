package com.ankush.cleancity.MailStuff;

import com.ankush.cleancity.Wastes.EmailedWaste;
import com.ankush.cleancity.Wastes.Waste;
import org.apache.catalina.webresources.FileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@RestController
@RequestMapping("java/api/mail")
public class MailController {
    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    MailBean mailBean;

    @RequestMapping("send")
    public ResponseEntity<?> sendMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cleancityconnect@healthierme.in");
        message.setTo("ankush.dewangan22@vit.edu");
        message.setSubject("HELLO :)");
        message.setText("HELLO WORLD");
        emailSender.send(message);

        return ResponseEntity.ok("SENT");
    }


    @RequestMapping("send/{email}/{msg}")
    public ResponseEntity<?> sendMailTo(@PathVariable String email, @PathVariable String msg) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cleancityconnect@healthierme.in");
        message.setTo(email);
        message.setSubject("HELLO :)");
        message.setText(msg);
        emailSender.send(message);
        return ResponseEntity.ok("SENT:\t" + email + "\t" + msg);
    }

    @RequestMapping("sendF/{email}")
    public ResponseEntity<?> sendFormatted(@PathVariable String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cleancityconnect@healthierme.in");
        message.setTo(email);
        message.setSubject("HELLO :)");
//        message.setText(String.format());
        emailSender.send(message);
        return ResponseEntity.ok("SENT:\t" + email);
    }

    @RequestMapping("test/{email}")
    public ResponseEntity<?> test(@PathVariable String email) {
        Waste w = new Waste();
        w.setLocation("Abc");
        mailBean.notifyComplaint(new EmailedWaste(w, email));
        return ResponseEntity.ok("OK");
    }

}
