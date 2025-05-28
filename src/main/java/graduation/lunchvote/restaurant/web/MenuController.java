package graduation.lunchvote.restaurant.web;

import graduation.lunchvote.restaurant.service.MenuService;
import graduation.lunchvote.restaurant.to.MenuTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static graduation.lunchvote.restaurant.util.MenuUtil.createTo;
import static graduation.lunchvote.restaurant.util.MenuUtil.getTos;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MenuController {

    static final String REST_URL = "/api/menus";

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/today")
    public List<MenuTo> getTodayMenus() {
        log.info("get menus for today");
        return getTos(menuService.getMenusForDate(LocalDate.now()));
    }

    @GetMapping("/by-date")
    public List<MenuTo> getMenusByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get menus for date {}", date);
        return getTos(menuService.getMenusForDate(date));
    }

    @GetMapping("/{id}")
    public MenuTo get(@PathVariable int id) {
        log.info("get menu with id {}", id);
        return createTo(menuService.get(id));
    }
}