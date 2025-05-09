package graduation.lunchvote.user.service;

import graduation.lunchvote.common.error.NotFoundException;
import graduation.lunchvote.user.model.Menu;
import graduation.lunchvote.user.model.Restaurant;
import graduation.lunchvote.user.repository.MenuRepository;
import graduation.lunchvote.user.repository.RestaurantRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuService(MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Возвращает список всех меню, зарегистрированных в системе.
     *
     * @return список объектов {@link Menu}, содержащий все меню
     */
    @Cacheable(cacheNames = "menus")
    public List<Menu> getAll() {
        return menuRepository.getAll();
    }

    /**
     * Возвращает меню по его уникальному идентификатору.
     *
     * @param id идентификатор меню, которое необходимо получить
     * @return объект {@link Menu} с соответствующим идентификатором
     * @throws NotFoundException если меню с указанным идентификатором не найдено
     */
    public Menu get(int id) {
        return menuRepository.get(id)
                .orElseThrow(() -> new NotFoundException("Menu with id=" + id + " not found"));
    }

    /**
     * Возвращает список меню для указанной даты.
     *
     * @param date дата, для которой необходимо получить меню
     * @return список объектов {@link Menu}, соответствующих указанной дате
     */
    @Cacheable(cacheNames = "menus")
    public List<Menu> getMenusForDate(LocalDate date) {
        return menuRepository.findAllByDate(date);
    }

    /**
     * Создает новое меню или обновляет существующее для указанного ресторана и даты.
     *
     * <p>
     * Метод проверяет наличие ресторана с использованием {@code restaurantRepository.getExisted(restaurantId)}.
     * Затем он пытается найти меню для данного ресторана и даты с помощью {@code menuRepository.findByRestaurantAndDate(restaurantId, date)}.
     * Если меню найдено, его элементы обновляются данными из параметра {@code items}. Если меню отсутствует, создается новое меню с указанной датой и рестораном.
     * После этого обновленное или новое меню сохраняется через {@code menuRepository.save()}.
     * </p>
     *
     * @param restaurantId идентификатор ресторана, для которого создается или обновляется меню
     * @param date         дата, на которую распространяется меню
     * @param items        карта, содержащая наименования позиций меню и их цены
     * @return сохраненный объект {@link Menu}, представляющий обновленное или созданное меню
     */
    @CacheEvict(cacheNames = "menus", allEntries = true)
    @Transactional
    public Menu createOrUpdate(Integer restaurantId, LocalDate date, Map<String, Double> items) {
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);

        Menu menu = menuRepository.findByRestaurantAndDate(restaurantId, date)
                .orElse(new Menu(date, restaurant));

        menu.setItems(items);
        return menuRepository.save(menu);
    }

    /**
     * Удаляет меню по заданному идентификатору.
     *
     * @param id идентификатор меню, которое требуется удалить
     */
    @CacheEvict(cacheNames = "menus", allEntries = true)
    public void delete(int id) {
        menuRepository.deleteExisted(id);
    }
}
