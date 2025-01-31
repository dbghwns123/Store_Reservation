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

    private String title;

    private String detail;

    private Integer rating;

    public Review(User user, Store store, String title, String detail, Integer rating) {
        this.user = user;
        this.store = store;
        this.title = title;
        this.detail = detail;
        this.rating = rating;
    }

    public void updateReview(String newTitle, String newDetail, Integer newRating) {
        this.title = newTitle;
        this.detail = newDetail;
        this.rating = newRating;
    }
}
