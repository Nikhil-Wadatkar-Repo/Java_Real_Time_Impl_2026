package com.mco.service;
import com.mco.dto.TransferRequest;
import com.mco.entity.Account;
import com.mco.repo.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FraudCheckService {

    @Transactional(propagation = Propagation.SUPPORTS)
    public void check(TransferRequest req) {
        System.out.println("Fraud check done");
    }
}
