package graduation.lunchvote.vote.to;

import graduation.lunchvote.common.to.BaseTo;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {

    LocalDate date;

    int userId;

    int restaurantId;

    public VoteTo(Integer id, LocalDate date, int userId, int restaurantId) {
        super(id);
        this.date = date;
        this.userId = userId;
        this.restaurantId = restaurantId;
    }
}