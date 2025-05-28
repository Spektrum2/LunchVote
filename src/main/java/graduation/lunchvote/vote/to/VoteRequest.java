package graduation.lunchvote.vote.to;

import jakarta.validation.constraints.NotNull;

public record VoteRequest(@NotNull Integer restaurantId) {

}