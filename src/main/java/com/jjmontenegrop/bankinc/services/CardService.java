package com.jjmontenegrop.bankinc.services;

import com.jjmontenegrop.bankinc.exceptions.CardNotFoundException;

import java.math.BigDecimal;

public interface CardService {

    String generateCardNumber(String productId);

    void activateCard(String cardNumber) throws CardNotFoundException;

    void blockCard(String cardId) throws CardNotFoundException;

    void rechargeCard(String cardId, BigDecimal balance) throws CardNotFoundException;

    String getCardBalance(String cardId) throws CardNotFoundException;
}
