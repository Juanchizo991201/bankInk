package com.jjmontenegrop.bankinc.entities.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionAnulationRequestDTO {

    private String cardId;
    private String transactionId;
}
