package com.jjmontenegrop.bankinc.services;

import com.jjmontenegrop.bankinc.entities.Card;
import com.jjmontenegrop.bankinc.entities.dto.PurchaseRequestResponseDTO;
import com.jjmontenegrop.bankinc.entities.Transaction;
import com.jjmontenegrop.bankinc.exceptions.CardNotFoundException;
import com.jjmontenegrop.bankinc.exceptions.InsufficientFundsException;
import com.jjmontenegrop.bankinc.exceptions.TransactionAlreadyAnnulatedException;
import com.jjmontenegrop.bankinc.exceptions.TransactionNotFoundException;
import com.jjmontenegrop.bankinc.repositories.CardRepository;
import com.jjmontenegrop.bankinc.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    TransactionRepository transactionRepository;
    CardRepository cardRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, CardRepository cardRepository) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
    }

    @Transactional
    public Long purchase(String cardId, BigDecimal price) {

        Optional<Card> card = cardRepository.findById(cardId);

        if (card.isEmpty()) {
            return 0L;
        }

        Transaction transaction = new Transaction();

        Card purchaseCard = card.get();

        transaction.setCardId(cardId);
        transaction.setPrice(price);

        BigDecimal actualBalance = purchaseCard.getBalance();
        BigDecimal newBalance = actualBalance.subtract(price);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        purchaseCard.setBalance(newBalance);

        cardRepository.save(purchaseCard);
        transactionRepository.save(transaction);

        return transaction.getTransactionId();
    }


    public PurchaseRequestResponseDTO getTransaction(String transactionId) {

        Optional<Transaction> transaction = transactionRepository.findById(transactionId);

        if (transaction.isEmpty()) {
            return null;
        }
        Transaction transactionResponse = transaction.get();

        PurchaseRequestResponseDTO response = new PurchaseRequestResponseDTO();

        response.setCardId(transactionResponse.getCardId());
        response.setPrice(transactionResponse.getPrice());

        return response;
    }

    public void anulation(String cardNumber, String transactionId) {

        Optional<Card> card = cardRepository.findById(cardNumber);
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);

        if (card.isEmpty()) {
            throw new CardNotFoundException("Card with number: " + cardNumber + " not found");
        }

        if (transaction.isEmpty()) {
            throw new TransactionNotFoundException("Transaction with id: " + transactionId + " not found");
        }

        Card cardAnnulation = card.get();
        Transaction transactionAnnulation = transaction.get();

        if (transactionAnnulation.getIsAnnulled()) {
            throw new TransactionAlreadyAnnulatedException("Transaction already annulated");
        }

        BigDecimal actualBalance = cardAnnulation.getBalance();
        BigDecimal newBalance = actualBalance.add(transactionAnnulation.getPrice());

        cardAnnulation.setBalance(newBalance);

        cardRepository.save(cardAnnulation);

        transactionAnnulation.setAnulated();
        transactionRepository.save(transactionAnnulation);
    }
}
