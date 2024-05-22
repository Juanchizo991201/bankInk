package com.jjmontenegrop.bankinc.entities.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CardRechargeRequestDTO {

    private String cardId;
    private BigDecimal balance;

}
