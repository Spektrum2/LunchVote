package graduation.lunchvote.user.repository;

import graduation.lunchvote.common.BaseRepository;
import graduation.lunchvote.user.model.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MenuRepository extends BaseRepository<Menu> {
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = :restaurantId AND m.date = :date")
    Optional<Menu> findByRestaurantAndDate(@Param("restaurantId") Integer restaurantId,
                                           @Param("date") LocalDate date);

    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant WHERE m.date =?1")
    List<Menu> findAllByDate(LocalDate date);

    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant")
    List<Menu> getAll();

    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant WHERE m.id =?1")
    Optional<Menu> get(int id);
}
