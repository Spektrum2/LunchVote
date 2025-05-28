package graduation.lunchvote.restaurant;

import graduation.lunchvote.MatcherFactory;
import graduation.lunchvote.restaurant.model.Restaurant;

import java.util.List;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menus", "votes");
    public static final int RESTAURANT1_ID = 1;
    public static final int NOT_FOUND = 100;
    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "Вкусная История","ул. Пушкинская, 17, Москва" );
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT1_ID + 1, "Дворцовые Угощения","ул. Лермонтова, 23, Санкт-Петербург" );
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT1_ID + 2, "Сказочный Вкус","ул. Чехова, 5, Казань" );
    public static final Restaurant restaurant4 = new Restaurant(RESTAURANT1_ID + 3, "Золотой Котелок","ул. Толстого, 12, Новосибирск" );
    public static final Restaurant restaurant5 = new Restaurant(RESTAURANT1_ID + 4, "Волшебный Обед","ул. Тургенева, 8, Екатеринбург" );
    public static final List<Restaurant> restaurants = List.of(restaurant1, restaurant2, restaurant3, restaurant4, restaurant5);

    public static Restaurant getNew() {
        return new Restaurant(null, "newRestaurant","Moscow" );
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "updateRestaurant","Canada");
    }
}
