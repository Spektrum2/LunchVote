package graduation.lunchvote.user.service;

import graduation.lunchvote.user.model.Restaurant;
import graduation.lunchvote.user.repository.RestaurantRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Создаёт новый объект ресторана и сохраняет его в хранилище.
     *
     * @param restaurant объект ресторана, который необходимо создать.
     * @return сохранённый объект {@link Restaurant}.
     */
    @CacheEvict(cacheNames = "restaurants", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    /**
     * Возвращает список всех ресторанов.
     *
     * @return список объектов {@link Restaurant}, представляющих все рестораны.
     */
    @Cacheable(cacheNames = "restaurants")
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    /**
     * Возвращает ресторан по заданному идентификатору.
     *
     * @param id уникальный идентификатор ресторана.
     * @return объект {@link Restaurant}, соответствующий заданному идентификатору.
     */
    public Restaurant get(int id) {
        return restaurantRepository.getExisted(id);
    }

    /**
     * Обновляет данные существующего ресторана.
     *
     * @param id         идентификатор ресторана, который требуется обновить.
     * @param restaurant объект с новыми данными для ресторана.
     * @return обновлённый объект {@link Restaurant}.
     */
    @CacheEvict(cacheNames = "restaurants", allEntries = true)
    @Transactional
    public Restaurant update(int id, Restaurant restaurant) {
        Restaurant oldRestaurant = get(id);
        oldRestaurant.setName(restaurant.getName());
        oldRestaurant.setAddress(restaurant.getAddress());
        return restaurantRepository.save(oldRestaurant);
    }

    /**
     * Удаляет ресторан с заданным идентификатором.
     *
     * @param id уникальный идентификатор ресторана, который необходимо удалить.
     */
    @CacheEvict(cacheNames = "restaurants", allEntries = true)
    public void delete(int id) {
        restaurantRepository.deleteExisted(id);
    }
}
