package org.walkmanx21.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Sessions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Session {

    @Id
    @Column(name="id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name="expires_at")
    private LocalDateTime localDateTime;
}
