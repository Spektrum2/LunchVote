package graduation.lunchvote.restaurant.model;

import graduation.lunchvote.common.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_date", "restaurant_id"}, name = "menu_unique_date_restaurant_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant"})
public class Menu extends BaseEntity {

    @Column(name = "menu_date", nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @CollectionTable(name = "menu_item", joinColumns = @JoinColumn(name = "menu_id"))
    @ElementCollection(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotEmpty
    private List<MenuItem> items;

    public Menu(Integer id, LocalDate date, Restaurant restaurant, List<MenuItem> items) {
        super(id);
        this.date = date;
        this.restaurant = restaurant;
        this.items = items != null ? items : Collections.emptyList();
    }

    public Menu(LocalDate date, Restaurant restaurant) {
        this(null, date, restaurant, Collections.emptyList());
    }
}