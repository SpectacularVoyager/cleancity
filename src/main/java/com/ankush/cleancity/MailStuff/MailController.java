package com.ankush.cleancity.MailStuff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("java/api/mail")
public class MailController {
    @Autowired
    private JavaMailSender emailSender;

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
    public ResponseEntity<?> sendMailTo(@PathVariable String email,@PathVariable String msg) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cleancityconnect@healthierme.in");
        message.setTo(email);
        message.setSubject("HELLO :)");
        message.setText(msg);
        emailSender.send(message);
        return ResponseEntity.ok("SENT:\t"+email+"\t"+msg);
    }

}
