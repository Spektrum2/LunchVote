package graduation.lunchvote.restaurant.web;

import graduation.lunchvote.restaurant.model.Menu;
import graduation.lunchvote.restaurant.service.MenuService;
import graduation.lunchvote.restaurant.to.MenuRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminMenuController {

    static final String REST_URL = "/api/admin/menus";

    private final MenuService menuService;

    public AdminMenuController(MenuService menuService) {
        this.menuService = menuService;
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Menu createOrUpdateMenu(@RequestParam Integer restaurantId,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   @Valid @RequestBody MenuRequest menuRequest) {
        log.info("Menu created/updated for restaurantId={}, date={}", restaurantId, date);
        return menuService.createOrUpdate(restaurantId, date, menuRequest.items());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete menu with id {}", id);
        menuService.delete(id);
    }
}