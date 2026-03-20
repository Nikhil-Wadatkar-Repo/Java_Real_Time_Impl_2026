package com.mco.controller;

import com.mco.dto.TransferRequest;
import com.mco.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionController {

    private final TransferService service;

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest req) {
        service.transferMoney(req);
        return "SUCCESS";
    }
}