package com.jjmontenegrop.bankinc.controllers;

import com.jjmontenegrop.bankinc.entities.dto.CardEnrollmentRequestDTO;
import com.jjmontenegrop.bankinc.entities.dto.CardRechargeRequestDTO;
import com.jjmontenegrop.bankinc.services.CardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/card")
public class CardController {

    CardServiceImpl cardServiceImpl;

    @Autowired
    public CardController(CardServiceImpl cardServiceImpl) {
        this.cardServiceImpl = cardServiceImpl;
    }

    @GetMapping("/{productId}/number")
    public String generateCardNumber (@PathVariable String productId) {
        return cardServiceImpl.generateCardNumber(productId);
    }

    @PostMapping("/enroll")
    public ResponseEntity<String> enrollCard(@RequestBody CardEnrollmentRequestDTO request) {

        String cardNumber = request.getCardId();
        cardServiceImpl.activateCard(cardNumber);
        String responseMessage = "Card activated";

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<String> deleteCard(@PathVariable String cardId) {

        cardServiceImpl.blockCard(cardId);

        String responseMessage = "Card blocked";

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/balance")
    public ResponseEntity<String> rechargeCard(@RequestBody CardRechargeRequestDTO request) {

        String cardNumber = request.getCardId();
        BigDecimal balance = request.getBalance();

        cardServiceImpl.rechargeCard(cardNumber, balance);

        String responseMessage = "Card recharged";

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping("/balance/{cardId}")
    public ResponseEntity<String> getCardBalance(@PathVariable String cardId) {
        String balance = cardServiceImpl.getCardBalance(cardId);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }
}
