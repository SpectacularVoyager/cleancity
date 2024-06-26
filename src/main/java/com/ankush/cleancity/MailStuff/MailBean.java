package com.ankush.cleancity.MailStuff;

import com.ankush.cleancity.Wastes.EmailedWaste;
import com.ankush.cleancity.Wastes.Waste;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@Slf4j
public class MailBean {

    private static final String COMPLAINT_RAISED = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>New Complaint Raised</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f4f4;
                        color: #333;
                        margin: 0;
                        padding: 0;
                    }
                    .container {
                        width: 100%;
                        max-width: 600px;
                        margin: 20px auto;
                        background-color: #fff;
                        padding: 20px;
                        border-radius: 8px;
                        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                    }
                    .header {
                        background-color: #ff8c00;
                        color: #fff;
                        padding: 10px 20px;
                        border-radius: 8px 8px 0 0;
                        text-align: center;
                    }
                    .content {
                        padding: 20px;
                    }
                    .footer {
                        margin-top: 20px;
                        text-align: center;
                        color: #999;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>New Complaint Raised</h1>
                    </div>
                    <div class="content">
                        <p>Dear Manager,</p>
                        <p>A new complaint ___LINK___ has been raised in your ward. Please review the details and take the necessary actions to resolve it.</p>
                        <p>Thank you for your attention.</p>
                    </div>
                    <div class="footer">
                        <p>&copy; 2024 Clean City Connect. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """;
    private static final String COMPLAINT_RESOLVED = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Complaint Resolved</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f4f4;
                        color: #333;
                        margin: 0;
                        padding: 0;
                    }
                    .container {
                        width: 100%;
                        max-width: 600px;
                        margin: 20px auto;
                        background-color: #fff;
                        padding: 20px;
                        border-radius: 8px;
                        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                    }
                    .header {
                        background-color: #28a745;
                        color: #fff;
                        padding: 10px 20px;
                        border-radius: 8px 8px 0 0;
                        text-align: center;
                    }
                    .content {
                        padding: 20px;
                    }
                    .footer {
                        margin-top: 20px;
                        text-align: center;
                        color: #999;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Complaint Resolved</h1>
                    </div>
                    <div class="content">
                        <p>Dear User,</p>
                        <p>We are pleased to inform you that your complaint ___LINK___ has been resolved. Thank you for helping us keep our city clean.</p>
                        <p>Thank you for using Clean City Connect.</p>
                    </div>
                    <div class="footer">
                        <p>&copy; 2024 Clean City Connect. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """;
    private static final String COMPLAINT_INVALID_PERSON = """
            <!DOCTYPE html>
                               <html lang="en">
                               <head>
                                   <meta charset="UTF-8">
                                   <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                   <title>Invalid Complaint</title>
                                   <style>
                                       body {
                                           font-family: Arial, sans-serif;
                                           background-color: #f4f4f4;
                                           color: #333;
                                           margin: 0;
                                           padding: 0;
                                       }
                                       .container {
                                           width: 100%;
                                           max-width: 600px;
                                           margin: 20px auto;
                                           background-color: #fff;
                                           padding: 20px;
                                           border-radius: 8px;
                                           box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                                       }
                                       .header {
                                           background-color: #e60000;
                                           color: #fff;
                                           padding: 10px 20px;
                                           border-radius: 8px 8px 0 0;
                                           text-align: center;
                                       }
                                       .content {
                                           padding: 20px;
                                       }
                                       .footer {
                                           margin-top: 20px;
                                           text-align: center;
                                           color: #999;
                                       }
                                   </style>
                               </head>
                               <body>
                                   <div class="container">
                                       <div class="header">
                                           <h1>Invalid Complaint</h1>
                                       </div>
                                       <div class="content">
                                           <p>Dear User,</p>
                                           <p>Your complaint ___LINK___ has been marked as invalid. Please make sure to provide accurate information and valid proof of the issue you are reporting.</p>
                                           <p>Thank you for using Clean City Connect.</p>
                                       </div>
                                       <div class="footer">
                                           <p>&copy; 2024 Clean City Connect. All rights reserved.</p>
                                       </div>
                                   </div>
                               </body>
                               </html>
            """;
    private static final String COMPLAINT_INVALID_AI = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Invalid Image Detected</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f4f4;
                        color: #333;
                        margin: 0;
                        padding: 0;
                    }
                    .container {
                        width: 100%;
                        max-width: 600px;
                        margin: 20px auto;
                        background-color: #fff;
                        padding: 20px;
                        border-radius: 8px;
                        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                    }
                    .header {
                        background-color: #0073e6;
                        color: #fff;
                        padding: 10px 20px;
                        border-radius: 8px 8px 0 0;
                        text-align: center;
                    }
                    .content {
                        padding: 20px;
                    }
                    .footer {
                        margin-top: 20px;
                        text-align: center;
                        color: #999;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Invalid Image Detected</h1>
                    </div>
                    <div class="content">
                        <p>Dear User,</p>
                        <p>Our system has detected that the image you uploaded is invalid. Please upload a clear image of the issue you are reporting.</p>
                        <p>___LINK___</p>

                        <p>Thank you for using Clean City Connect.</p>
                    </div>
                    <div class="footer">
                        <p>&copy; 2024 Clean City Connect. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
                        """;


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

    public void sendMimeMail(String email, String subject, String text, Waste w) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setText(text, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setFrom("cleancityconnect@healthierme.in");
            emailSender.send(message);
            new Thread(new RunableMimeMessageMail(message, emailSender)).start();
        } catch (MessagingException e) {
            log.error("MESSAGING EXCEPTION", e);
        }
    }


    public void notifyComplaint(EmailedWaste w) {
        String msg = String.format("Complaint with id %d complete", w.getWaste().getId());
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("cleancityconnect@healthierme.in");
//        message.setTo(w.getEmail());
//        message.setSubject("Complaint Acknowledged");
//        message.setText(msg);
//        new Thread(new RunnableSendSimpleMail(message, emailSender)).start();
//        sendSimpleEmail(w.getEmail(), "Complaint Acknowledged", msg, w.getWaste());
        sendMimeMail(w.getEmail(), "Your complaint has been resolved", getFormatted(COMPLAINT_RESOLVED, w.getWaste()), w.getWaste());

    }

    public void notifyInvalidPerson(EmailedWaste w) {
        sendMimeMail(w.getEmail(), "COMPLAINT FLAGGED INVALID ", getFormatted(COMPLAINT_INVALID_PERSON, w.getWaste()), w.getWaste());
    }

    public void notifyInvalidAI(EmailedWaste w) {
//        sendSimpleEmail(w.getEmail(), "Invalid AI", "invalidated", w.getWaste());
        sendMimeMail(w.getEmail(), "COMPLAINT FLAGGED INVALID BY AI", getFormatted(COMPLAINT_INVALID_AI, w.getWaste()), w.getWaste());


    }

    public void complaintRaised(String manager, Waste w) {
        log.info("MANAGER MAILED {}", manager);
//        sendSimpleEmail(manager, "Complaint Raised At " + w.getLocation(), String.format(COMPLAINT_RAISED, w.getLocation()), w);
        sendMimeMail(manager, "Complaint Raised At " + w.getLocation(), getFormatted(COMPLAINT_RAISED, w), w);
    }

    String ip = "localhost";

    public String getFormatted(String s, Waste w) {
//        log.info("INFO");
//        return s.replace("___LINK___", String.format("<a href=\"https://%s/profile/complaints/%s\"></a>", ip, w.getId()));
        return s;
    }


}
