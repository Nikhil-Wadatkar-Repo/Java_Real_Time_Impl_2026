package com.mco.service;
import com.mco.dto.TransferRequest;
import com.mco.entity.Account;
import com.mco.repo.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void notifyUser(TransferRequest req) {
        System.out.println("Notification sent");
    }
}
