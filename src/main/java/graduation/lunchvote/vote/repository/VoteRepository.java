package graduation.lunchvote.vote.repository;

import graduation.lunchvote.common.BaseRepository;
import graduation.lunchvote.vote.model.Vote;
import graduation.lunchvote.vote.to.VoteResult;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {
    Optional<Vote> findByUserIdAndDate(Integer userId, LocalDate date);

    List<Vote> findAllByDate(LocalDate date);

    List<Vote> findAllByUserId(Integer userId);

    boolean existsByUserIdAndDate(Integer userId, LocalDate today);

    @Query("SELECT new graduation.lunchvote.vote.to.VoteResult(v.restaurant.id, COUNT(v)) " +
            "FROM Vote v " +
            "WHERE v.date = :date " +
            "GROUP BY v.restaurant.id " +
            "ORDER BY COUNT(v) DESC")
    List<VoteResult> findVoteCountsByDate(@Param("date") LocalDate date);
}