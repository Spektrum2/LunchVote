package graduation.lunchvote.vote.model;

import graduation.lunchvote.common.model.BaseEntity;
import graduation.lunchvote.restaurant.model.Restaurant;
import graduation.lunchvote.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "vote_date"}, name = "vote_unique_user_created_at_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    @Column(name = "vote_date", nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    public Vote(Integer id, LocalDate date, User user, Restaurant restaurant) {
        super(id);
        this.date = date;
        this.user = user;
        this.restaurant = restaurant;
    }
}