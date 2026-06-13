package com.mco.email_notification_service.service;

import com.mco.email_notification_service.dto.EmailRequest;
import com.mco.email_notification_service.dto.EmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public EmailResponse sendEmail(EmailRequest emailRequest) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailRequest.getTo());
            message.setSubject(emailRequest.getSubject());
            message.setText(emailRequest.getBody());

            if (emailRequest.getCc() != null && emailRequest.getCc().length > 0) {
                message.setCc(emailRequest.getCc());
            }

            if (emailRequest.getBcc() != null && emailRequest.getBcc().length > 0) {
                message.setBcc(emailRequest.getBcc());
            }

            mailSender.send(message);

            String emailId = UUID.randomUUID().toString();
            log.info("Email sent successfully. Email ID: {}", emailId);

            return new EmailResponse(true, "Email sent successfully", emailId);
        } catch (Exception e) {
            log.error("Error sending email", e);
            return new EmailResponse(false, "Error sending email: " + e.getMessage(), null);
        }
    }
}
