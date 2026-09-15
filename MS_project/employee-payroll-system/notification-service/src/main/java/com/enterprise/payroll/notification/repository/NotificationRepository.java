package com.enterprise.payroll.notification.repository;

import com.enterprise.payroll.notification.entity.Notification;
import com.enterprise.payroll.notification.entity.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByEmployeeId(Long employeeId, Pageable pageable);

    boolean existsByTypeAndSourceEventKey(NotificationType type, String sourceEventKey);
}
