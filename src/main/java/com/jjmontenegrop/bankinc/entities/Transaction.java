package com.jjmontenegrop.bankinc.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_seq", allocationSize = 1, initialValue = 1000)
    @Column(name = "transaction_id", nullable = false, unique = true)
    private Long transactionId;


    @Column(name = "card_id", nullable = false)
    private String cardId;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "is_annulled", nullable = false)
    private Boolean isAnnulled;

    @PrePersist
    private void prePersist() {
        this.transactionDate = LocalDateTime.now();
        this.isAnnulled = false;
    }

    public void setAnulated() {
        this.isAnnulled = true;
    }
}
