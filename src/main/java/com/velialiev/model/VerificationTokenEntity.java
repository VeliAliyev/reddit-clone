package com.velialiev.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "token")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verificationId;

    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity userEntity;

    private Instant expiryDate;
}
