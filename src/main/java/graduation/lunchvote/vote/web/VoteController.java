package graduation.lunchvote.vote.web;

import graduation.lunchvote.app.AuthUser;
import graduation.lunchvote.vote.model.Vote;
import graduation.lunchvote.vote.service.VoteService;
import graduation.lunchvote.vote.to.VoteRequest;
import graduation.lunchvote.vote.to.VoteResult;
import graduation.lunchvote.vote.to.VoteTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static graduation.lunchvote.vote.util.VoteUtil.createTo;
import static graduation.lunchvote.vote.util.VoteUtil.getTos;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class VoteController {

    static final String REST_URL = "/api/votes";

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> create(@RequestBody VoteRequest voteRequest,
                                       @AuthenticationPrincipal AuthUser authUser) {
        log.info("Received vote request for restaurant id = {} by user id = {}", voteRequest.restaurantId(), authUser.id());
        Vote created = voteService.create(authUser.id(), voteRequest.restaurantId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/today")
                .build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody VoteRequest voteRequest,
                       @AuthenticationPrincipal AuthUser authUser) {
        log.info("Received vote update request for restaurant id = {} by user id = {}", voteRequest.restaurantId(), authUser.id());
        voteService.update(authUser.id(), voteRequest.restaurantId());
    }

    @GetMapping("/today")
    public VoteTo getTodayVote(@AuthenticationPrincipal AuthUser authUser) {
        log.info("Get today's vote for user id = {}", authUser.id());
        return createTo(voteService.getTodayVote(authUser.id()));
    }

    @GetMapping("/history")
    public List<VoteTo> getVoteHistory(@AuthenticationPrincipal AuthUser authUser) {
        log.info("Get vote history for user id = {}", authUser.id());
        return getTos(voteService.getVoteHistory(authUser.id()));
    }

    @GetMapping("/results")
    public List<VoteResult> getResults(@RequestParam(required = false)
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Received request to get voting results for date = {}", date);
        return voteService.getVotingResults(date);
    }
}
