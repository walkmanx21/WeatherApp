package org.walkmanx21.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Sessions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {

    @Id
    @Column(name="id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name="expiresAt")
    private LocalDateTime localDateTime;
}
