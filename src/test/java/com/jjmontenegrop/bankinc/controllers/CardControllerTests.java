package com.jjmontenegrop.bankinc.controllers;

import com.jjmontenegrop.bankinc.entities.dto.CardEnrollmentRequestDTO;
import com.jjmontenegrop.bankinc.entities.dto.CardRechargeRequestDTO;
import com.jjmontenegrop.bankinc.exceptions.CardNotFoundException;
import com.jjmontenegrop.bankinc.exceptions.InvalidRechargeAmountException;
import com.jjmontenegrop.bankinc.services.CardServiceImpl;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CardControllerTests {

    @InjectMocks
    private CardController cardController;

    @Mock
    private CardServiceImpl cardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should generate card number for valid product id")
    void shouldGenerateCardNumberForValidProductId() {
        String productId = "validProductId";
        String expectedCardNumber = "1234567890";
        when(cardService.generateCardNumber(productId)).thenReturn(expectedCardNumber);

        String actualCardNumber = cardController.generateCardNumber(productId);

        assertEquals(expectedCardNumber, actualCardNumber);
    }

    @Test
    @DisplayName("Should return empty string for invalid product id")
    void shouldReturnEmptyStringForInvalidProductId() {
        String productId = "invalidProductId";
        String expectedCardNumber = "";
        when(cardService.generateCardNumber(productId)).thenReturn(expectedCardNumber);

        String actualCardNumber = cardController.generateCardNumber(productId);

        assertEquals(expectedCardNumber, actualCardNumber);
    }

    @Test
    @DisplayName("Should return OK status when card is enrolled successfully")
    void shouldReturnOkStatusWhenCardIsEnrolledSuccessfully() {
        CardEnrollmentRequestDTO request = new CardEnrollmentRequestDTO();
        request.setCardId("validCardId");

        doNothing().when(cardService).activateCard(anyString());

        ResponseEntity<String> responseEntity = cardController.enrollCard(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Card activated", responseEntity.getBody());
    }

    @Test
    @DisplayName("Should throw exception when card enrollment fails")
    void shouldThrowExceptionWhenCardEnrollmentFails() {
        CardEnrollmentRequestDTO request = new CardEnrollmentRequestDTO();
        request.setCardId("invalidCardId");

        doThrow(new RuntimeException()).when(cardService).activateCard(anyString());

        assertThrows(RuntimeException.class, () -> cardController.enrollCard(request));
    }

    @Test
    @DisplayName("Should return OK status when card is blocked successfully")
    void shouldReturnOkStatusWhenCardIsBlockedSuccessfully() {
        String cardId = "validCardId";

        doNothing().when(cardService).blockCard(cardId);

        ResponseEntity<String> responseEntity = cardController.deleteCard(cardId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Card blocked", responseEntity.getBody());
    }

    @Test
    @DisplayName("Should throw exception when card blocking fails")
    void shouldThrowExceptionWhenCardBlockingFails() {
        String cardId = "invalidCardId";

        doThrow(new RuntimeException()).when(cardService).blockCard(cardId);

        assertThrows(RuntimeException.class, () -> cardController.deleteCard(cardId));
    }

    @Test
    @DisplayName("Should return OK status when card is recharged successfully")
    void shouldReturnOkStatusWhenCardIsRechargedSuccessfully() {
        CardRechargeRequestDTO request = new CardRechargeRequestDTO();
        request.setCardId("validCardId");
        request.setBalance(new BigDecimal("100.00"));

        doNothing().when(cardService).rechargeCard(anyString(), any(BigDecimal.class));

        ResponseEntity<String> responseEntity = cardController.rechargeCard(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Card recharged", responseEntity.getBody());
    }

    @Test
    @DisplayName("Should throw exception when card recharge fails")
    void shouldThrowExceptionWhenCardRechargeFails() {
        CardRechargeRequestDTO request = new CardRechargeRequestDTO();
        request.setCardId("invalidCardId");
        request.setBalance(new BigDecimal("100.00"));

        doThrow(new RuntimeException()).when(cardService).rechargeCard(anyString(), any(BigDecimal.class));

        assertThrows(RuntimeException.class, () -> cardController.rechargeCard(request));
    }

    @Test
    @DisplayName("Should throw exception when card recharge amount is negative or invalid")
    void shouldThrowExceptionWhenCardRechargeAmountIsNegativeOrInvalid() {
        CardRechargeRequestDTO request = new CardRechargeRequestDTO();
        request.setCardId("validCardId");
        request.setBalance(new BigDecimal("-100.00"));

        doThrow(new InvalidRechargeAmountException("Recharge amount should be valid")).when(cardService).rechargeCard(anyString(), any(BigDecimal.class));

        assertThrows(InvalidRechargeAmountException.class, () -> cardController.rechargeCard(request));
    }

    @Test
    @DisplayName("Should return OK status and correct balance when card balance is retrieved successfully")
    void shouldReturnOkStatusAndCorrectBalanceWhenCardBalanceIsRetrievedSuccessfully() {
        String cardId = "validCardId";
        String expectedBalance = "100.00";

        when(cardService.getCardBalance(cardId)).thenReturn(expectedBalance);

        ResponseEntity<String> responseEntity = cardController.getCardBalance(cardId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedBalance, responseEntity.getBody());
    }

    @Test
    @DisplayName("Should throw CardNotFoundException when card does not exist")
    void shouldThrowCardNotFoundExceptionWhenCardDoesNotExist() {
        String cardId = "invalidCardId";

        when(cardService.getCardBalance(cardId)).thenThrow(new CardNotFoundException("Card not found"));

        assertThrows(CardNotFoundException.class, () -> cardController.getCardBalance(cardId));
    }
}
