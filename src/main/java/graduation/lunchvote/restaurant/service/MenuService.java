package graduation.lunchvote.restaurant.service;

import graduation.lunchvote.common.error.NotFoundException;
import graduation.lunchvote.restaurant.model.Menu;
import graduation.lunchvote.restaurant.model.MenuItem;
import graduation.lunchvote.restaurant.model.Restaurant;
import graduation.lunchvote.restaurant.repository.MenuRepository;
import graduation.lunchvote.restaurant.repository.RestaurantRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuService(MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Retrieves a menu by its unique identifier.
     *
     * @param id the identifier of the menu to retrieve
     * @return the {@link Menu} object with the corresponding identifier
     * @throws NotFoundException if no menu with the specified ID is found
     */
    public Menu get(int id) {
        return menuRepository.get(id)
                .orElseThrow(() -> new NotFoundException("No menu found for today"));
    }

    /**
     * Retrieves a list of menus for the specified date.
     *
     * @param date the date for which to retrieve menus
     * @return a list of {@link Menu} objects corresponding to the specified date
     */
    @Cacheable(value = "menus", key = "#date")
    public List<Menu> getMenusForDate(LocalDate date) {
        return menuRepository.findAllByDate(date);
    }

    /**
     * Creates or updates a restaurant's menu for the specified date.
     *
     * @param restaurantId the identifier of the restaurant for which the menu is created/updated
     * @param date         the date for which the menu is valid
     * @param items        the list of menu items with their prices
     * @return the saved menu
     * @throws NotFoundException if no restaurant with the specified ID is found
     */
    @Transactional
    @CacheEvict(value = "menus", key = "#date")
    public Menu createOrUpdate(Integer restaurantId, LocalDate date, List<MenuItem> items) {
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);

        Menu menu = menuRepository.findByRestaurantAndDate(restaurantId, date)
                .orElse(new Menu(date, restaurant));

        menu.setItems(items);
        return menuRepository.save(menu);
    }

    /**
     * Deletes a menu by the specified identifier.
     *
     * @param id the identifier of the menu to delete
     */
    @CacheEvict(value = "menus", allEntries = true)
    public void delete(int id) {
        menuRepository.deleteExisted(id);
    }
}