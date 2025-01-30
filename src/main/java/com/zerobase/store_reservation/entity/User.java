package com.zerobase.store_reservation.entity;

import com.zerobase.store_reservation.type.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<Store> stores;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;

    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;


    public User(String name, String password, String email, UserRole role) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.userRole = role;
    }
}
