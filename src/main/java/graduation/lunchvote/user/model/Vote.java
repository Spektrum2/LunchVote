package graduation.lunchvote.user.model;

import com.fasterxml.jackson.annotation.*;
import graduation.lunchvote.common.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date"}, name = "vote_unique_user_created_at_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    @Column(nullable = false)
    @NotNull
    private LocalDate date;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    public Vote(Integer id, LocalDate date, User user, Restaurant restaurant) {
        super(id);
        this.date = date;
        this.user = user;
        this.restaurant = restaurant;
    }

    @JsonSetter("user")
    public void setUserData(Object userData) {
        if (userData instanceof Integer) {
            this.user = new User();
            this.user.setId((Integer) userData);
        } else if (userData instanceof User) {
            this.user = (User) userData;
        }
    }

    @JsonSetter("restaurant")
    public void setRestaurantData(Object restaurantData) {
        if (restaurantData instanceof Integer) {
            this.restaurant = new Restaurant();
            this.restaurant.setId((Integer) restaurantData);
        } else if (restaurantData instanceof Restaurant) {
            this.restaurant = (Restaurant) restaurantData;
        }
    }
}
