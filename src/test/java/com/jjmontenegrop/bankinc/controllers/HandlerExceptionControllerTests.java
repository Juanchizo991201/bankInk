package com.jjmontenegrop.bankinc.controllers;

import com.jjmontenegrop.bankinc.exceptions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
 class HandlerExceptionControllerTests {

    @InjectMocks
    private HandlerExceptionController handlerExceptionController;

    @Test
    @DisplayName("Should return NOT_FOUND status when CardNotFoundException is thrown")
    void shouldReturnNotFoundStatusWhenCardNotFoundExceptionIsThrown() {
        CardNotFoundException exception = new CardNotFoundException("Card not found");

        ResponseEntity<String> responseEntity = handlerExceptionController.handleCardNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Card not found", responseEntity.getBody());
    }

    @Test
    @DisplayName("Should return BAD_REQUEST status when InvalidProductIdException is thrown")
    void shouldReturnBadRequestStatusWhenInvalidProductIdExceptionIsThrown() {
        InvalidProductIdException exception = new InvalidProductIdException("Invalid product id");

        ResponseEntity<String> responseEntity = handlerExceptionController.handleInvalidProductIdException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid product id", responseEntity.getBody());
    }

    @Test
    @DisplayName("Should return BAD_REQUEST status when CardAlreadyActivatedException is thrown")
    void shouldReturnBadRequestStatusWhenCardAlreadyActivatedExceptionIsThrown() {
        CardAlreadyActivatedException exception = new CardAlreadyActivatedException("Card already activated");

        ResponseEntity<String> responseEntity = handlerExceptionController.handleCardAlreadyActivatedException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Card already activated", responseEntity.getBody());
    }

    @Test
    @DisplayName("Should return BAD_REQUEST status when CardAlreadyBlockedException is thrown")
    void shouldReturnBadRequestStatusWhenCardAlreadyBlockedExceptionIsThrown() {
        CardAlreadyBlockedException exception = new CardAlreadyBlockedException("Card already blocked");

        ResponseEntity<String> responseEntity = handlerExceptionController.handleCardAlreadyBlockedException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Card already blocked", responseEntity.getBody());
    }

    @Test
    @DisplayName("Should return BAD_REQUEST status when CardNotAvailableException is thrown")
    void shouldReturnBadRequestStatusWhenCardNotAvailableExceptionIsThrown() {
        CardNotAvailableException exception = new CardNotAvailableException("Card not available");

        ResponseEntity<String> responseEntity = handlerExceptionController.handleCardNotAvailableException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Card not available", responseEntity.getBody());
    }

    @Test
    @DisplayName("Should return BAD_REQUEST status when InvalidRechargeAmountException is thrown")
    void shouldReturnBadRequestStatusWhenInvalidRechargeAmountExceptionIsThrown() {
        InvalidRechargeAmountException exception = new InvalidRechargeAmountException("Invalid recharge amount");

        ResponseEntity<String> responseEntity = handlerExceptionController.handleInvalidRechargeAmountException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid recharge amount", responseEntity.getBody());
    }

    @Test
    @DisplayName("Should return BAD_REQUEST status when InsufficientFundsException is thrown")
    void shouldReturnBadRequestStatusWhenInsufficientFundsExceptionIsThrown() {
        InsufficientFundsException exception = new InsufficientFundsException("Insufficient funds");

        ResponseEntity<String> responseEntity = handlerExceptionController.handleInsufficientFundsException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Insufficient funds", responseEntity.getBody());
    }

    @Test
    @DisplayName("Should return NOT_FOUND status when TransactionNotFoundException is thrown")
    void shouldReturnNotFoundStatusWhenTransactionNotFoundExceptionIsThrown() {
        TransactionNotFoundException exception = new TransactionNotFoundException("Transaction not found");

        ResponseEntity<String> responseEntity = handlerExceptionController.handleCardOrTransactionNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Transaction not found", responseEntity.getBody());
    }

    @Test
    @DisplayName("Should return BAD_REQUEST status when TransactionAlreadyAnnulatedException is thrown")
    void shouldReturnBadRequestStatusWhenTransactionAlreadyAnnulatedExceptionIsThrown() {
        TransactionAlreadyAnnulatedException exception = new TransactionAlreadyAnnulatedException("Transaction already annulated");

        ResponseEntity<String> responseEntity = handlerExceptionController.handleTransactionAlreadyAnnulatedException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Transaction already annulated", responseEntity.getBody());
    }
}
