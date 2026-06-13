package com.mco.service;

import com.mco.dto.TransferRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final DebitService debitService;
    private final CreditService creditService;
    private final AuditService auditService;
    private final NotificationService notificationService;
    private final FraudCheckService fraudCheckService;
    private final LimitService limitService;
    private final BackupService backupService;

    @Transactional
    public void transferMoney(TransferRequest req) {

        limitService.checkLimit(req);       // MANDATORY
        fraudCheckService.check(req);       // SUPPORTS

        debitService.debit(req);            // REQUIRED
        creditService.credit(req);          // REQUIRED

        try {
            backupService.backup(req);      // NESTED
        } catch (Exception e) {
            System.out.println("Backup failed, continuing...");
        }

        auditService.audit(req);            // REQUIRES_NEW
        notificationService.notifyUser(req);// NOT_SUPPORTED
    }
}
