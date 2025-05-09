package graduation.lunchvote.user.web;

import graduation.lunchvote.app.AuthUser;
import graduation.lunchvote.user.model.Vote;
import graduation.lunchvote.user.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class VoteController {

    static final String REST_URL = "/api/votes";

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<Vote> vote(@RequestParam Integer restaurantId,
                                     @AuthenticationPrincipal AuthUser authUser) {
        log.info("Received vote request for restaurant id = {} by user id = {}", restaurantId, authUser.id());
        Vote vote = voteService.vote(authUser.id(), restaurantId);
        return ResponseEntity.ok(vote);
    }

    @GetMapping("/results")
    public List<String> getResults(@RequestParam(required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Received request to get voting results for date = {}", date);
        return voteService.getVotingResults(date);
    }

    @GetMapping
    public List<Vote> getAll() {
        log.info("get all votes");
        return voteService.getAll();
    }

    @GetMapping("/{id}")
    public Vote get(@PathVariable Integer id) {
        log.info("get voting result for id = {}", id);
        return voteService.get(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        log.info("delete voting result for id = {}", id);
        voteService.delete(id);
    }
}
