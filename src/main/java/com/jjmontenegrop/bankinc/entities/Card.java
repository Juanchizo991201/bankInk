package com.jjmontenegrop.bankinc.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Card {

    @Id
    @Column(name = "card_id", nullable = false, unique = true)
    private String cardId;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column (nullable = false)
    private Boolean isActive;

    @Column (nullable = false)
    private Boolean isBlocked;

    @Column
    private BigDecimal balance;

    public Card(String cardNumber) {
        this.cardId = cardNumber;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime currentDate = LocalDateTime.now();
        this.expirationDate = currentDate.plusYears(3);
        this.isActive = false;
        this.isBlocked = false;
        this.balance = BigDecimal.valueOf(0);
    }

    public boolean isAvailable() {
        return !this.getIsActive() || this.getIsBlocked();
    }
}
