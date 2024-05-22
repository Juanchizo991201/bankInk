package com.jjmontenegrop.bankinc.services;

import com.jjmontenegrop.bankinc.entities.Card;
import com.jjmontenegrop.bankinc.exceptions.*;
import com.jjmontenegrop.bankinc.repositories.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CardServiceImplTests {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Generate card number tests
    @Test
    @DisplayName("Generate card number with valid product ID")
    void shouldGenerateCardNumberWithValidProductId() {
        String validProductId = "123456";
        Card card = new Card();

        when(cardRepository.save(any(Card.class))).thenReturn(card);

        String cardNumber = cardService.generateCardNumber(validProductId);

        assertNotNull(cardNumber);
        assertEquals(16, cardNumber.length());
        assertTrue(cardNumber.startsWith(validProductId));

        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    @DisplayName("Generate card number with invalid product ID")
    void shouldThrowExceptionWhenProductIdIsInvalid() {
        String invalidProductId = "123";

        assertThrows(InvalidProductIdException.class, () -> cardService.generateCardNumber(invalidProductId));

        verify(cardRepository, never()).save(any(Card.class));
    }

    // Activate card tests
    @Test
    @DisplayName("Test activate a card that does not exists")
    void testActivateCardNotFound() {
        String cardNumber = "1234567890123456";
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CardNotFoundException.class, () -> {
            cardService.activateCard(cardNumber);
        });

        String expectedMessage = "Card with number: " + cardNumber + " not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Test activate a card that is already activated")
    void testActivateCardAlreadyActivated() {
        String cardNumber = "1234567890123456";
        Card card = new Card(cardNumber, LocalDateTime.now(), true, false, BigDecimal.valueOf(0));
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.of(card));

        Exception exception = assertThrows(CardAlreadyActivatedException.class, () -> cardService.activateCard(cardNumber));

        String expectedMessage = "Card with number: " + cardNumber + " is already activated";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Should activate card when card is not active")
    void shouldActivateCardWhenCardIsNotActive() {
        String cardId = "validCardId";
        Card card = new Card(cardId);
        card.setIsActive(false);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        cardService.activateCard(cardId);

        assertTrue(card.getIsActive());
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    @DisplayName("Should not activate card when card is already active")
    void shouldNotActivateCardWhenCardIsAlreadyActive() {
        String cardId = "validCardId";
        Card card = new Card(cardId);
        card.setIsActive(true);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        assertThrows(CardAlreadyActivatedException.class, () -> cardService.activateCard(cardId));
        verify(cardRepository, never()).save(card);
    }

    @Test
    @DisplayName("Should throw CardNotFoundException when card does not exist")
    void shouldThrowCardNotFoundExceptionWhenCardDoesNotExist() {
        String cardId = "invalidCardId";

        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.activateCard(cardId));
        verify(cardRepository, never()).save(any(Card.class));
    }

    // Block card tests
    @Test
    @DisplayName("Test block a card that does not exists")
    void testBlockCardNotFound() {
        String cardNumber = "1234567890123456";
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CardNotFoundException.class, () -> {
            cardService.blockCard(cardNumber);
        });

        String expectedMessage = "Card with number: " + cardNumber + " not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Test block a card that is already blocked")
    void testBlockCardAlreadyBlocked() {
        String cardNumber = "1234567890123456";
        Card card = new Card(cardNumber, LocalDateTime.now(), true, true, BigDecimal.valueOf(0));
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.of(card));

        Exception exception = assertThrows(CardAlreadyBlockedException.class, () -> {
            cardService.blockCard(cardNumber);
        });

        String expectedMessage = "Card with number: " + cardNumber + " is already blocked";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Test block a card successfully")
    void testBlockCardSuccess() {
        String cardNumber = "1234567890123456";
        Card card = new Card(cardNumber, LocalDateTime.now(), true, false, BigDecimal.valueOf(0));
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.of(card));

        cardService.blockCard(cardNumber);

        if (cardRepository.findById(cardNumber).isEmpty()) {
            fail();
        }
        assertTrue(cardRepository.findById(cardNumber).get().getIsBlocked());

        verify(cardRepository, times(1)).save(card);
    }

    // Recharge card tests
    @Test
    @DisplayName("Test recharge a unknown card")
    void testRechargeCardNotFound() {
        String cardNumber = "1234567890123456";
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CardNotFoundException.class, () -> {
            cardService.rechargeCard(cardNumber, BigDecimal.valueOf(100));
        });

        String expectedMessage = "Card with number: " + cardNumber + " not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Test recharge a inactive or blocked card")
    void testRechargeCardNotAvailable() {
        String cardNumber = "1234567890123456";
        Card card = new Card(cardNumber);
        card.setIsActive(false);
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.of(card));

        Exception exception = assertThrows(CardNotAvailableException.class, () -> {
            cardService.rechargeCard(cardNumber, BigDecimal.valueOf(100));
        });

        String expectedMessage = "Card with number: " + cardNumber + " not available for recharge";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Should throw InvalidRechargeAmountException when balance is zero or negative")
    void shouldThrowInvalidRechargeAmountExceptionWhenBalanceIsZeroOrNegative() {
        String cardId = "validCardId";
        BigDecimal balance = BigDecimal.ZERO;

        Card card = new Card(cardId);
        card.setIsActive(true);
        card.setIsBlocked(false);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        assertThrows(InvalidRechargeAmountException.class, () -> cardService.rechargeCard(cardId, balance));
    }

    @Test
    @DisplayName("Test recharge a card successfully")
    void testRechargeCardSuccess() {
        String cardNumber = "1234567890123456";
        Card card = new Card(cardNumber, LocalDateTime.now(), true, false, BigDecimal.valueOf(50));
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.of(card));

        cardService.rechargeCard(cardNumber, BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(150), card.getBalance());
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    @DisplayName("Test get card balance with unknown card")
    void testGetCardBalanceCardNotFound() {
        String cardNumber = "1234567890123456";
        when(cardRepository.findById(cardNumber)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CardNotFoundException.class, () -> {
            cardService.getCardBalance(cardNumber);
        });

        String expectedMessage = "Card with number: " + cardNumber + " not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // get card balance tests
    @Test
    @DisplayName("Should return card balance when card id is valid and card is available")
    void shouldReturnCardBalanceWhenCardIdIsValidAndCardIsAvailable() {
        String cardId = "validCardId";
        Card card = new Card(cardId);
        card.setIsActive(true);
        card.setIsBlocked(false);
        card.setBalance(BigDecimal.valueOf(100.00));

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        String balance = cardService.getCardBalance(cardId);

        assertNotNull(balance);
        assertEquals("100.0", balance);
    }

    @Test
    @DisplayName("Should throw CardNotFoundException when card id is invalid")
    void shouldThrowCardNotFoundExceptionWhenCardIdIsInvalid() {
        String cardId = "invalidCardId";

        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.getCardBalance(cardId));
    }

    @Test
    @DisplayName("Should throw CardNotAvailableException when card is not active")
    void shouldThrowCardNotAvailableExceptionWhenCardIsNotActive() {
        String cardId = "validCardId";
        Card card = new Card(cardId);
        card.setIsActive(false);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        assertThrows(CardNotAvailableException.class, () -> cardService.getCardBalance(cardId));
    }

    @Test
    @DisplayName("Should throw CardNotAvailableException when card is blocked")
    void shouldThrowCardNotAvailableExceptionWhenCardIsBlocked() {
        String cardId = "validCardId";
        Card card = new Card(cardId);
        card.setIsActive(true);
        card.setIsBlocked(true);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        assertThrows(CardNotAvailableException.class, () -> cardService.getCardBalance(cardId));
    }
}


