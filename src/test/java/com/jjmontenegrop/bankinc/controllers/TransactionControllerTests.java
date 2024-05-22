package com.jjmontenegrop.bankinc.controllers;

import com.jjmontenegrop.bankinc.entities.dto.PurchaseRequestResponseDTO;
import com.jjmontenegrop.bankinc.entities.dto.TransactionAnulationRequestDTO;
import com.jjmontenegrop.bankinc.services.TransactionServiceImpl;
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
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)

class TransactionControllerTests {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return OK status and purchase id when purchase is successful")
    void shouldReturnOkStatusAndPurchaseIdWhenPurchaseIsSuccessful() {
        PurchaseRequestResponseDTO request = new PurchaseRequestResponseDTO();
        request.setCardId("validCardId");
        request.setPrice(new BigDecimal("100.00"));

        when(transactionService.purchase(anyString(), any(BigDecimal.class))).thenReturn(1L);

        ResponseEntity<String> responseEntity = transactionController.purchase(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Purchase id: 1", responseEntity.getBody());
    }

    @Test
    @DisplayName("Should return transaction details when transaction id is valid")
    void shouldReturnTransactionDetailsWhenTransactionIdIsValid() {
        String transactionId = "validTransactionId";
        PurchaseRequestResponseDTO expectedTransaction = new PurchaseRequestResponseDTO();
        expectedTransaction.setCardId("validCardId");
        expectedTransaction.setPrice(new BigDecimal("100.00"));

        when(transactionService.getTransaction(anyString())).thenReturn(expectedTransaction);

        ResponseEntity<PurchaseRequestResponseDTO> responseEntity = transactionController.getTransaction(transactionId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedTransaction, responseEntity.getBody());
    }

    @Test
    @DisplayName("Should return OK status when transaction anulation is successful")
    void shouldReturnOkStatusWhenTransactionAnulationIsSuccessful() {
        TransactionAnulationRequestDTO request = new TransactionAnulationRequestDTO();
        request.setCardId("validCardId");
        request.setTransactionId("validTransactionId");

        doNothing().when(transactionService).anulation(anyString(), anyString());

        ResponseEntity<String> responseEntity = transactionController.anulation(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Transaction anulated", responseEntity.getBody());
    }
}
