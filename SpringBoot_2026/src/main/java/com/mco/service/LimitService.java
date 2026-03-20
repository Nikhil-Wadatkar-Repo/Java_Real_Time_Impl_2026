package com.mco.service;
import com.mco.dto.TransferRequest;
import com.mco.entity.Account;
import com.mco.repo.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LimitService {

    @Transactional(propagation = Propagation.MANDATORY)
    public void checkLimit(TransferRequest req) {
        if (req.getAmount() > 100000) {
            throw new RuntimeException("Limit exceeded");
        }
    }
}
