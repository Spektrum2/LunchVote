package graduation.lunchvote.restaurant.to;

import graduation.lunchvote.restaurant.model.MenuItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record MenuRequest(@NotEmpty @Valid List<MenuItem> items) {
}