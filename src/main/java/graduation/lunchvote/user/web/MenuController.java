package graduation.lunchvote.user.web;

import graduation.lunchvote.user.model.Menu;
import graduation.lunchvote.user.service.MenuService;
import graduation.lunchvote.user.to.ItemsDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MenuController {

    static final String REST_URL = "/api/menus";

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    List<Menu> getAll() {
        log.info("get all menus");
        return menuService.getAll();
    }

    @GetMapping("/today")
    public List<Menu> getTodayMenus() {
        log.info("get menus for today");
        return menuService.getMenusForDate(LocalDate.now());
    }

    @GetMapping("/by-date")
    public List<Menu> getMenusByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get menus for date {}", date);
        return menuService.getMenusForDate(date);
    }

    @GetMapping("/{id}")
    public Menu get(@PathVariable int id) {
        log.info("get menu with id {}", id);
        return menuService.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Menu createOrUpdateMenu(@RequestParam Integer restaurantId,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   @Valid @RequestBody ItemsDTO itemsDTO) {
        log.info("Menu created/updated for restaurantId={}, date={}", restaurantId, date);
        return menuService.createOrUpdate(restaurantId, date, itemsDTO.getItems());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete menu with id {}", id);
        menuService.delete(id);
    }
}
