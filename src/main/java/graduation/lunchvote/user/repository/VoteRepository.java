package graduation.lunchvote.user.repository;

import graduation.lunchvote.common.BaseRepository;
import graduation.lunchvote.user.model.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends BaseRepository<Vote> {
    Optional<Vote> findByUserIdAndDate(Integer userId, LocalDate date);

    List<Vote> findAllByDate(LocalDate date);

    @EntityGraph(attributePaths = {"user", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v")
    List<Vote> getAll();

    @EntityGraph(attributePaths = {"user", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.id =?1")
    Optional<Vote> get(int id);
}
