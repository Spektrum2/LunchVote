package graduation.lunchvote.restaurant.service;

import graduation.lunchvote.restaurant.model.Restaurant;
import graduation.lunchvote.restaurant.repository.RestaurantRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Creates a new restaurant or updates an existing one.
     *
     * @param restaurant the restaurant object to be created or updated
     * @return the saved {@link Restaurant} entity
     */
    @CacheEvict(cacheNames = "restaurants", allEntries = true)
    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    /**
     * Retrieves all restaurants.
     *
     * @return list of all {@link Restaurant} entities
     */
    @Cacheable(cacheNames = "restaurants")
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    /**
     * Retrieves a restaurant by its unique identifier.
     *
     * @param id the unique identifier of the restaurant
     * @return the {@link Restaurant} entity with the specified ID
     */
    public Restaurant get(int id) {
        return restaurantRepository.getExisted(id);
    }

    /**
     * Deletes a restaurant with the specified identifier.
     *
     * @param id the unique identifier of the restaurant to be deleted
     */
    @CacheEvict(cacheNames = "restaurants", allEntries = true)
    public void delete(int id) {
        restaurantRepository.deleteExisted(id);
    }
}