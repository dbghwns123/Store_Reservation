package com.zerobase.store_reservation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Review extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 300)
    private String detail;

    @NotNull
    @Min(1)
    @Max(5)
    private Long rating;

}
