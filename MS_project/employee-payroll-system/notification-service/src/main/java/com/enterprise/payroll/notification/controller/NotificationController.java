package com.enterprise.payroll.notification.controller;

import com.enterprise.payroll.notification.dto.NotificationResponse;
import com.enterprise.payroll.notification.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Page<NotificationResponse>> getForEmployee(@PathVariable Long employeeId, Pageable pageable) {
        Page<NotificationResponse> page = notificationRepository.findByEmployeeId(employeeId, pageable)
                .map(n -> new NotificationResponse(n.getId(), n.getEmployeeId(), n.getType(), n.getMessage(), n.getSentAt()));
        return ResponseEntity.ok(page);
    }
}
