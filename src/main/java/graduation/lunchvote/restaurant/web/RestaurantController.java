package graduation.lunchvote.restaurant.web;

import graduation.lunchvote.restaurant.model.Restaurant;
import graduation.lunchvote.restaurant.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantController {

    static final String REST_URL = "/api/restaurants";

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return restaurantService.getAll();
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get restaurant with id {}", id);
        return restaurantService.get(id);
    }
}
