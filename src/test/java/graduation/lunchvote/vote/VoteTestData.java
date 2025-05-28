package graduation.lunchvote.vote;

import graduation.lunchvote.MatcherFactory;
import graduation.lunchvote.restaurant.model.Restaurant;
import graduation.lunchvote.user.model.User;
import graduation.lunchvote.vote.model.Vote;

import java.time.LocalDate;
import java.util.List;

import static graduation.lunchvote.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static graduation.lunchvote.user.UserTestData.USER_ID;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user", "restaurant.menus", "restaurant.votes");
    public static final int VOTE1_ID = 1;
    public static final Vote vote1 = new Vote(VOTE1_ID, LocalDate.now(), new User(USER_ID, null, null, null), new Restaurant(RESTAURANT1_ID, null, null));
    public static final Vote vote2 = new Vote(VOTE1_ID + 1, LocalDate.now(), new User(USER_ID + 1, null, null, null), new Restaurant(RESTAURANT1_ID + 1, null, null));
    public static final Vote voteUpdated = new Vote(1, LocalDate.now(), new User(USER_ID, null, null, null), new Restaurant(RESTAURANT1_ID + 4, "Волшебный Обед", "ул. Тургенева, 8, Екатеринбург"));
    public static final List<Vote> votes = List.of(vote1, vote2);

    public static Vote getNew() {
        return new Vote(null, LocalDate.now(), new User(USER_ID, null, null, null), new Restaurant(RESTAURANT1_ID + 4, "Волшебный Обед", "ул. Тургенева, 8, Екатеринбург"));
    }

}
