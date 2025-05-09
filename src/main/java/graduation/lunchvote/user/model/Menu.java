package graduation.lunchvote.user.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import graduation.lunchvote.common.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "date"}, name = "menu_unique_restaurant_date_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

    @Column(nullable = false)
    @NotNull
    private LocalDate date;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @CollectionTable(name = "menu_item", joinColumns = @JoinColumn(name = "menu_id"))
    @MapKeyColumn(name = "dish_name")
    @Column(name = "price")
    @ElementCollection(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotEmpty
    private Map<String, Double> items;

    public Menu(LocalDate date, Restaurant restaurant) {
        this.date = date;
        this.restaurant = restaurant;
    }

    public Menu(Integer id, LocalDate date, Restaurant restaurant, Map<String, Double> items) {
        this(date, restaurant);
        this.id = id;
        this.items = items;
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
