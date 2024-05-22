package com.jjmontenegrop.bankinc.entities.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PurchaseRequestResponseDTO {

    private String cardId;
    private BigDecimal price;

}
