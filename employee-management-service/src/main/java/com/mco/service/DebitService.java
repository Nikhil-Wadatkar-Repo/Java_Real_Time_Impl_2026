package com.mco.service;
import com.mco.dto.TransferRequest;
import com.mco.entity.Account;
import com.mco.repo.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DebitService {

    private final AccountRepository repo;

    @Transactional(propagation = Propagation.REQUIRED)
    public void debit(TransferRequest req) {
        Account acc = repo.findById(req.getFromAccount()).orElseThrow();
        acc.setBalance(acc.getBalance() - req.getAmount());
        repo.save(acc);
    }
}
