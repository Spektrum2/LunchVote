package graduation.lunchvote.restaurant.to;

import graduation.lunchvote.common.to.BaseTo;
import graduation.lunchvote.restaurant.model.MenuItem;
import lombok.EqualsAndHashCode;
import lombok.Value;


import java.time.LocalDate;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class MenuTo extends BaseTo {

    LocalDate date;

    int restaurantId;

    List<MenuItem> menuItems;

    public MenuTo(Integer id, LocalDate date, int restaurantId, List<MenuItem> menuItems) {
        super(id);
        this.date = date;
        this.restaurantId = restaurantId;
        this.menuItems = menuItems;
    }
}