package com.jjmontenegrop.bankinc.services;

import com.jjmontenegrop.bankinc.entities.dto.PurchaseRequestResponseDTO;

import java.math.BigDecimal;

public interface TransactionService {

    Long purchase(String cardId, BigDecimal price);

    PurchaseRequestResponseDTO getTransaction(String transactionId);

    void anulation(String cardNumber, String transactionId);
}
