package com.example.demo;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "processed-event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class ProcessedEventEntity {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false, unique = true)
    @Nonnull
    private String messageId;
    @Column(nullable = false)
    @Nonnull
    private String productId;
}
