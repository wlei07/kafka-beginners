package com.appsdeveloperblog.estore.transfers.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "transfer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Setter
public class TransferEntity {
    @Id
    @Column(nullable = false)
    private String transferId;

    @Column(nullable = false)
    @Nonnull
    private String senderId;

    @Column(nullable = false)
    @Nonnull
    private String recepientId;

    @Column(nullable = false)
    @Nonnull
    private BigDecimal amount;
}
