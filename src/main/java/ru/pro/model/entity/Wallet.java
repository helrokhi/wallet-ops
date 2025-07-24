package ru.pro.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.NONE;

@Table(indexes = @Index(name = "idx_wallet_id", columnList = "id"))
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(precision = 15, scale = 2, nullable = false)
    @Min(0)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(updatable = false)
    @Setter(NONE)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Setter(NONE)
    private LocalDateTime updatedAt;
}
