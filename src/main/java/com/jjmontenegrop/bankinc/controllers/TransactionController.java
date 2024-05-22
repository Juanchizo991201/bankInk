package com.jjmontenegrop.bankinc.controllers;

import com.jjmontenegrop.bankinc.entities.dto.PurchaseRequestResponseDTO;
import com.jjmontenegrop.bankinc.entities.dto.TransactionAnulationRequestDTO;
import com.jjmontenegrop.bankinc.services.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

TransactionServiceImpl transactionServiceImpl;

@Autowired
    public TransactionController(TransactionServiceImpl transactionServiceImpl) {
        this.transactionServiceImpl = transactionServiceImpl;
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> purchase(@RequestBody PurchaseRequestResponseDTO request) {

            String cardNumber = request.getCardId();
            BigDecimal price = request.getPrice();

            Long purchaseId = transactionServiceImpl.purchase(cardNumber, price);

            String responseMessage = "Purchase id: " + purchaseId;

            return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<PurchaseRequestResponseDTO> getTransaction(@PathVariable String transactionId) {

        PurchaseRequestResponseDTO transaction = transactionServiceImpl.getTransaction(transactionId);

        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/anulation")
    public ResponseEntity<String> anulation(@RequestBody TransactionAnulationRequestDTO request) {

        String cardNumber = request.getCardId();
        String transactionId = request.getTransactionId();

        transactionServiceImpl.anulation(cardNumber, transactionId);

        return ResponseEntity.ok("Transaction anulated");
    }

}
