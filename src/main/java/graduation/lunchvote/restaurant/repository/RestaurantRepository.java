package graduation.lunchvote.restaurant.repository;

import graduation.lunchvote.common.BaseRepository;
import graduation.lunchvote.restaurant.model.Restaurant;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

}