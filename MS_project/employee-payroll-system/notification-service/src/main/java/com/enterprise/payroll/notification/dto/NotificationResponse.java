package com.enterprise.payroll.notification.dto;

import com.enterprise.payroll.notification.entity.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        Long employeeId,
        NotificationType type,
        String message,
        LocalDateTime sentAt
) {
}
