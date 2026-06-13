package com.mco.email_notification_service.controller;

import com.mco.email_notification_service.dto.EmailRequest;
import com.mco.email_notification_service.dto.EmailResponse;
import com.mco.email_notification_service.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emails")
@Slf4j
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<EmailResponse> sendEmail(@RequestBody EmailReqgituest emailRequest) {
        log.info("Received email request to send email to: {}", emailRequest.getTo());
        EmailResponse response = emailService.sendEmail(emailRequest);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
