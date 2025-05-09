package graduation.lunchvote.user.web;

import graduation.lunchvote.AbstractControllerTest;
import graduation.lunchvote.user.model.Vote;
import graduation.lunchvote.user.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.*;

import static graduation.lunchvote.user.RestaurantTestData.RESTAURANT1_ID;
import static graduation.lunchvote.user.UserTestData.USER2_MAIL;
import static graduation.lunchvote.user.UserTestData.USER_MAIL;
import static graduation.lunchvote.user.VoteTestData.*;
import static graduation.lunchvote.user.web.VoteController.REST_URL;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteRepository voteRepository;

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    @WithUserDetails(USER_MAIL)
    void vote() throws Exception {
        Clock fixedClock = Clock.fixed(Instant.parse("2014-12-22T10:00:00.00Z"), ZoneId.of("UTC"));
        LocalTime mockTime = LocalTime.now(fixedClock);
        try (MockedStatic<LocalTime> mock = Mockito.mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
            mock.when(LocalTime::now).thenReturn(mockTime);


            ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                    .param("restaurantId", String.valueOf(RESTAURANT1_ID + 4))
                    .contentType(MediaType.APPLICATION_JSON));

            Vote created = VOTE_MATCHER.readFromJson(action);
            int newId = created.getId();

            Vote newVote = getNew();
            newVote.setId(newId);
            VOTE_MATCHER.assertMatch(voteRepository.getExisted(newId), newVote);
        }
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void voteAfterDeadline() throws Exception {
        Clock fixedClock = Clock.fixed(Instant.parse("2014-12-22T12:00:00.00Z"), ZoneId.of("UTC"));
        LocalTime mockTime = LocalTime.now(fixedClock);
        try (MockedStatic<LocalTime> mock = Mockito.mockStatic(LocalTime.class, Mockito.CALLS_REAL_METHODS)) {
            mock.when(LocalTime::now).thenReturn(mockTime);

            perform(MockMvcRequestBuilders.post(REST_URL)
                    .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnprocessableEntity());
        }
    }

    @Test
    @WithUserDetails(USER2_MAIL)
    void voteNew() throws Exception {
        Vote newVote = getNew();

        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID + 4))
                .contentType(MediaType.APPLICATION_JSON));

        Vote created = VOTE_MATCHER.readFromJson(action);
        int newId = created.getId();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(voteRepository.getExisted(newId), newVote);
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void getResults() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "results"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", containsInAnyOrder("Ресторан 1 : Рейтинг 1", "Ресторан 2 : Рейтинг 1")));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(votes));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + VOTE1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote1));
    }
}