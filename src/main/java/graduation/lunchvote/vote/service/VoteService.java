package graduation.lunchvote.vote.service;

import graduation.lunchvote.common.error.IllegalRequestDataException;
import graduation.lunchvote.common.error.NotFoundException;
import graduation.lunchvote.restaurant.model.Restaurant;
import graduation.lunchvote.restaurant.repository.RestaurantRepository;
import graduation.lunchvote.user.model.User;
import graduation.lunchvote.user.repository.UserRepository;
import graduation.lunchvote.vote.model.Vote;
import graduation.lunchvote.vote.repository.VoteRepository;
import graduation.lunchvote.vote.to.VoteResult;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class VoteService {
    private static final LocalTime VOTE_CHANGE_DEADLINE = LocalTime.of(11, 0);

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public VoteService(VoteRepository voteRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new vote for the specified user and restaurant.
     *
     * @param userId       the ID of the user casting the vote
     * @param restaurantId the ID of the restaurant being voted for
     * @return the newly created Vote object
     * @throws IllegalRequestDataException if the user has already voted today
     * @throws NotFoundException           if the user or restaurant is not found
     */
    @Transactional
    @CacheEvict(value = "votingResults", key = "#result?.date?.toString()")
    public Vote create(Integer userId, Integer restaurantId) {
        LocalDate today = LocalDate.now();

        if (voteRepository.existsByUserIdAndDate(userId, today)) {
            throw new IllegalRequestDataException("User has already voted today");
        }

        User user = userRepository.getExisted(userId);
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);

        Vote newVote = new Vote(null, today, user, restaurant);
        return voteRepository.save(newVote);
    }

    /**
     * Updates an existing vote for the specified user and restaurant.
     * Can only be performed before the voting deadline (11:00 AM).
     *
     * @param userId       the ID of the user changing their vote
     * @param restaurantId the ID of the new restaurant being voted for
     * @throws IllegalRequestDataException if attempted after the voting deadline
     * @throws NotFoundException           if no vote exists for today or restaurant is not found
     */
    @Transactional
    @CacheEvict(value = "votingResults", key = "T(java.time.LocalDate).now().toString()")
    public void update(Integer userId, Integer restaurantId) {
        final LocalDateTime currentMoment = LocalDateTime.now();
        final LocalDate currentDate = currentMoment.toLocalDate();
        final LocalTime currentTime = currentMoment.toLocalTime();

        if (currentTime.isAfter(VOTE_CHANGE_DEADLINE)) {
            throw new IllegalRequestDataException("Vote cannot be changed after 11:00 AM");
        }

        Vote vote = voteRepository.findByUserIdAndDate(userId, currentDate)
                .orElseThrow(() -> new NotFoundException("No vote found for today"));

        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        vote.setRestaurant(restaurant);

        voteRepository.save(vote);
    }

    /**
     * Retrieves today's vote for the specified user.
     *
     * @param userId the ID of the user whose vote to retrieve
     * @return the Vote object for today
     * @throws NotFoundException if no vote exists for today
     */
    public Vote getTodayVote(Integer userId) {
        return voteRepository.findByUserIdAndDate(userId, LocalDate.now())
                .orElseThrow(() -> new NotFoundException("No vote found for today"));
    }

    /**
     * Retrieves the complete voting history for the specified user.
     *
     * @param userId the ID of the user whose history to retrieve
     * @return a list of all Vote objects for the user
     */
    public List<Vote> getVoteHistory(Integer userId) {
        return voteRepository.findAllByUserId(userId);
    }

    /**
     * Retrieves voting results for the specified date (or today if date is null).
     *
     * @param date the date for which to retrieve results (null for today)
     * @return a list of VoteResult objects containing restaurant vote counts
     */
    @Cacheable(value = "votingResults", key = "#date?.toString() ?: T(java.time.LocalDate).now().toString()")
    public List<VoteResult> getVotingResults(LocalDate date) {
        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        return voteRepository.findVoteCountsByDate(queryDate);
    }
}