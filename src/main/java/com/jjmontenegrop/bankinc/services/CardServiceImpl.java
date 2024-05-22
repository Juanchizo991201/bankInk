package com.jjmontenegrop.bankinc.services;

import com.jjmontenegrop.bankinc.entities.Card;
import com.jjmontenegrop.bankinc.exceptions.*;
import com.jjmontenegrop.bankinc.repositories.CardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {

    CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public String generateCardNumber(String productId) {
        if (productId.length() < 6) {
            throw new InvalidProductIdException("Product ID should be at least 6 digits long.");
        }

        StringBuilder cardNumber = new StringBuilder(productId);
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < 10; i++) {
            cardNumber.append(secureRandom.nextInt(10));
        }

        Card card = new Card(cardNumber.toString());
        cardRepository.save(card);

        return cardNumber.toString();
    }

    @Transactional
    public void activateCard(String cardNumber) {

        Optional<Card> cardOptional = cardRepository.findById(cardNumber);

        if (cardOptional.isEmpty()) {
            throw new CardNotFoundException("Card with number: " + cardNumber + " not found");
        }

        Card card = cardOptional.get();

        if (card.getIsActive()) {
            throw new CardAlreadyActivatedException("Card with number: " + cardNumber + " is already activated");
        }

        card.setIsActive(true);
        cardRepository.save(card);
    }

    @Transactional
    public void blockCard(String cardId) {
        Optional<Card> card = cardRepository.findById(cardId);

        if (card.isEmpty()) {
            throw new CardNotFoundException("Card with number: " + cardId + " not found");
        }

        Card cardToInactivate = card.get();

        if (cardToInactivate.getIsBlocked()) {
            throw new CardAlreadyBlockedException("Card with number: " + cardId + " is already blocked");
        }

        cardToInactivate.setIsBlocked(true);
        cardRepository.save(cardToInactivate);

    }

    @Transactional
    public void rechargeCard(String cardId, BigDecimal balance) {
        Optional<Card> card = cardRepository.findById(cardId);

        if (card.isEmpty()) {
            throw new CardNotFoundException("Card with number: " + cardId + " not found");
        }

        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRechargeAmountException("Recharge amount should be valid");
        }

        Card cardToRecharge = card.get();

        if (cardToRecharge.isAvailable()) {
            throw new CardNotAvailableException("Card with number: " + cardId + " not available for recharge");
        }

        BigDecimal actualBalance = cardToRecharge.getBalance();
        BigDecimal newBalance = actualBalance.add(balance);

        cardToRecharge.setBalance(newBalance);
        cardRepository.save(cardToRecharge);

    }

    public String getCardBalance(String cardId) {
        Optional<Card> card = cardRepository.findById(cardId);

        if (card.isEmpty()) {
            throw new CardNotFoundException("Card with number: " + cardId + " not found");
        }

        Card cardToCheck = card.get();

        if (cardToCheck.isAvailable()) {
            throw new CardNotAvailableException("Card with number: " + cardId + " is inactivated or blocked");
        }

        return cardToCheck.getBalance().toString();
    }
}