package graduation.lunchvote.restaurant.web;

import graduation.lunchvote.restaurant.model.Restaurant;
import graduation.lunchvote.restaurant.repository.RestaurantRepository;
import graduation.lunchvote.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static graduation.lunchvote.common.validation.ValidationUtil.assureIdConsistent;
import static graduation.lunchvote.common.validation.ValidationUtil.checkIsNew;

@RestController
@RequestMapping(value = RestaurantAdminController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantAdminController {

    static final String REST_URL = "/api/admin/restaurants";

    private final RestaurantService restaurantService;

    private final RestaurantRepository restaurantRepository;

    public RestaurantAdminController(RestaurantService restaurantService, RestaurantRepository restaurantRepository) {
        this.restaurantService = restaurantService;
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        log.info("Create restaurant");
        checkIsNew(restaurant);
        Restaurant created = restaurantService.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody Restaurant restaurant) {
        log.info("Update restaurant with id {}", id);
        assureIdConsistent(restaurant, id);
        restaurantRepository.getExisted(restaurant.getId());
        restaurantService.save(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Delete restaurant with id {}", id);
        restaurantService.delete(id);
    }
}
