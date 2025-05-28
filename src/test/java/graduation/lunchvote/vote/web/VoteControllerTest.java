package graduation.lunchvote.vote.web;

import graduation.lunchvote.AbstractControllerTest;
import graduation.lunchvote.common.util.JsonUtil;
import graduation.lunchvote.vote.model.Vote;
import graduation.lunchvote.vote.repository.VoteRepository;
import graduation.lunchvote.vote.to.VoteRequest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static graduation.lunchvote.user.UserTestData.USER2_MAIL;
import static graduation.lunchvote.user.UserTestData.USER_MAIL;
import static graduation.lunchvote.vote.VoteTestData.*;
import static graduation.lunchvote.vote.web.VoteController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(USER_MAIL)
    void update() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.now().withHour(10).withMinute(0).withSecond(0).withNano(0);
        Clock fixedClock = Clock.fixed(Instant.parse(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS'Z'"))), ZoneId.of("UTC"));
        LocalDateTime mockTime = LocalDateTime.now(fixedClock);
        try (MockedStatic<LocalDateTime> mock = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mock.when(LocalDateTime::now).thenReturn(mockTime);

            VoteRequest voteRequest = new VoteRequest(5);
            perform(MockMvcRequestBuilders.put(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(voteRequest)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            VOTE_MATCHER.assertMatch(voteRepository.getExisted(VOTE1_ID), voteUpdated);
        }
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void updateAfterDeadline() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.now().withHour(11).withMinute(0).withSecond(0).withNano(0);
        Clock fixedClock = Clock.fixed(Instant.parse(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS'Z'"))), ZoneId.of("UTC"));
        LocalDateTime mockTime = LocalDateTime.now(fixedClock);
        try (MockedStatic<LocalDateTime> mock = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mock.when(LocalDateTime::now).thenReturn(mockTime);

            VoteRequest voteRequest = new VoteRequest(1);
            perform(MockMvcRequestBuilders.post(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(voteRequest)))
                    .andExpect(status().isUnprocessableEntity());
        }
    }

    @Test
    @WithUserDetails(USER2_MAIL)
    void create() throws Exception {
        Vote newVote = getNew();

        VoteRequest voteRequest = new VoteRequest(5);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(voteRequest)));

        Vote created = VOTE_MATCHER.readFromJson(action);
        int newId = created.getId();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(voteRepository.getExisted(newId), newVote);
    }
}