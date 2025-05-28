package graduation.lunchvote.restaurant.repository;

import graduation.lunchvote.common.BaseRepository;
import graduation.lunchvote.restaurant.model.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = :restaurantId AND m.date = :date")
    Optional<Menu> findByRestaurantAndDate(@Param("restaurantId") Integer restaurantId,
                                           @Param("date") LocalDate date);

    @Query("SELECT DISTINCT m FROM Menu m JOIN FETCH m.items WHERE m.date = :date")
    List<Menu> findAllByDate(@Param("date") LocalDate date);

    @Query("SELECT m FROM Menu m JOIN FETCH m.items WHERE m.id = :id")
    Optional<Menu> get(int id);
}