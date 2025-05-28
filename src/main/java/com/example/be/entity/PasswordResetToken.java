package com.example.be.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PasswordResetToken {
    @Id
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    public PasswordResetToken(String token, User user, int minutesToExpire) {
        this.token = token;
        this.user = user;
        this.expiryDate = LocalDateTime.now().plusMinutes(minutesToExpire);
    }

    private LocalDateTime expiryDate;
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
