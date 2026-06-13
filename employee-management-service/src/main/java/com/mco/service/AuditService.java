package com.mco.service;

import com.mco.dto.TransferRequest;
import com.mco.entity.AuditLog;
import com.mco.repo.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository repo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void audit(TransferRequest req) {
        repo.save(new AuditLog(null, "Transfer completed"));
    }
}
