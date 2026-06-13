package com.mco.service;

import com.mco.dto.TransferRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BackupService {

    @Transactional(propagation = Propagation.NESTED)
    public void backup(TransferRequest req) {
        System.out.println("Backup created");
        // Uncomment to test nested rollback
        // throw new RuntimeException("Backup failed");
    }
}
