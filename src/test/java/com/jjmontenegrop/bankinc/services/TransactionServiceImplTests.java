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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTests {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    @DisplayName("Purchase should return transaction ID when card exists and has sufficient funds")
    void purchaseShouldReturnTransactionIdWhenCardExistsAndHasSufficientFunds() {
        String cardId = "123";
        BigDecimal price = BigDecimal.valueOf(100);
        Card card = new Card();
        card.setBalance(BigDecimal.valueOf(200));

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        Long transactionId = transactionService.purchase(cardId, price);

        verify(cardRepository, times(1)).save(any(Card.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Purchase should return zero when card does not exist")
    void purchaseShouldReturnZeroWhenCardDoesNotExist() {
        String cardId = "123";
        BigDecimal price = BigDecimal.valueOf(100);

        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        Long transactionId = transactionService.purchase(cardId, price);

        assertEquals(0L, transactionId);
    }

    @Test
    @DisplayName("Purchase should throw InsufficientFundsException when card has insufficient funds")
    void purchaseShouldThrowInsufficientFundsExceptionWhenCardHasInsufficientFunds() {
        String cardId = "123";
        BigDecimal price = BigDecimal.valueOf(200);
        Card card = new Card();
        card.setBalance(BigDecimal.valueOf(100));

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        assertThrows(InsufficientFundsException.class, () -> transactionService.purchase(cardId, price));
    }

    @Test
    @DisplayName("Should successfully annulate transaction when card and transaction exist and transaction is not annulated")
    void shouldSuccessfullyAnnulateTransactionWhenCardAndTransactionExistAndTransactionIsNotAnnulated() {
        String cardId = "validCardId";
        String transactionId = "validTransactionId";
        Card card = new Card(cardId);
        card.setBalance(BigDecimal.valueOf(100));
        Transaction transaction = new Transaction();
        transaction.setCardId(cardId);
        transaction.setPrice(BigDecimal.valueOf(50));
        transaction.setIsAnnulled(false);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        transactionService.anulation(cardId, transactionId);

        verify(cardRepository, times(1)).save(any(Card.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw CardNotFoundException when card does not exist")
    void shouldThrowCardNotFoundExceptionWhenCardDoesNotExist() {
        String cardId = "invalidCardId";
        String transactionId = "validTransactionId";

        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> transactionService.anulation(cardId, transactionId));
    }

    @Test
    @DisplayName("Should return transaction details when transaction id is valid")
    void shouldReturnTransactionDetailsWhenTransactionIdIsValid() {
        String transactionId = "validTransactionId";
        Transaction transaction = new Transaction();
        transaction.setCardId("validCardId");
        transaction.setPrice(BigDecimal.valueOf(100.00));

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        PurchaseRequestResponseDTO response = transactionService.getTransaction(transactionId);

        assertNotNull(response);
        assertEquals(transaction.getCardId(), response.getCardId());
        assertEquals(transaction.getPrice(), response.getPrice());
    }

    @Test
    @DisplayName("Should return null when transaction id is invalid")
    void shouldReturnNullWhenTransactionIdIsInvalid() {
        String transactionId = "invalidTransactionId";

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        PurchaseRequestResponseDTO response = transactionService.getTransaction(transactionId);

        assertNull(response);
    }

    @Test
    @DisplayName("Should throw TransactionNotFoundException when transaction does not exist")
    void shouldThrowTransactionNotFoundExceptionWhenTransactionDoesNotExist() {
        String cardId = "validCardId";
        String transactionId = "invalidTransactionId";
        Card card = new Card(cardId);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.anulation(cardId, transactionId));
    }

    @Test
    @DisplayName("Should throw TransactionAlreadyAnnulatedException when transaction is already annulated")
    void shouldThrowTransactionAlreadyAnnulatedExceptionWhenTransactionIsAlreadyAnnulated() {
        String cardId = "validCardId";
        String transactionId = "validTransactionId";
        Card card = new Card(cardId);
        Transaction transaction = new Transaction();
        transaction.setCardId(cardId);
        transaction.setIsAnnulled(true);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        assertThrows(TransactionAlreadyAnnulatedException.class, () -> transactionService.anulation(cardId, transactionId));
    }
}