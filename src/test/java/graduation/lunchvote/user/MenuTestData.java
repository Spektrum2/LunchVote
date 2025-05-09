package graduation.lunchvote.user;

import graduation.lunchvote.MatcherFactory;
import graduation.lunchvote.user.model.Menu;
import graduation.lunchvote.user.model.Restaurant;
import graduation.lunchvote.user.to.ItemsDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static graduation.lunchvote.user.RestaurantTestData.*;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant");
    public static final int MENU1_ID = 1;
    public static final Menu menu1 = new Menu(
            MENU1_ID,
            LocalDate.now(),
            restaurant1,
            Map.of(
                    "Салат Цезарь с курицей", 350.0,
                    "Борщ с говядиной", 250.0,
                    "Стейк из свинины с картофельным пюре", 500.0,
                    "Компот из ягод", 100.0,
                    "Пирожное Наполеон", 150.0
                    )
    );
    public static final Menu menu2 = new Menu(
            MENU1_ID + 1,
            LocalDate.now(),
            restaurant2,
            Map.of(
                    "Греческий салат", 300.0,
                    "Суп с грибами и вермишелью", 220.0,
                    "Куриное филе в сливочно-грибном соусе с рисом", 450.0,
                    "Морс из клюквы", 90.0,
                    "Чизкейк", 180.0
            )
    );
    public static final Menu menu3 = new Menu(
            MENU1_ID + 2,
            LocalDate.now(),
            restaurant3,
            Map.of(
                    "Салат Оливье", 320.0,
                    "Крем-суп из тыквы", 200.0,
                    "Филе лосося на гриле с овощами", 600.0,
                    "Компот из сухофруктов", 120.0,
                    "Эклер с кремом", 130.0
            )
    );
    public static final Menu menu4 = new Menu(
            MENU1_ID + 3,
            LocalDate.now(),
            restaurant4,
            Map.of(
                    "Салат Винегрет", 280.0,
                    "Щи с говядиной", 230.0,
                    "Телятина с гречневой кашей и грибами", 520.0,
                    "Кисель", 100.0,
                    "Медовик", 160.0
            )
    );
    public static final Menu menu5 = new Menu(
            MENU1_ID + 4,
            LocalDate.now(),
            restaurant5,
            Map.of(
                    "Салат Капрезе", 310.0,
                    "Уха из красной рыбы", 270.0,
                    "Запеченная куриная грудка с овощами", 440.0,
                    "Лимонад", 90.0,
                    "Яблочный штрудель", 170.0
            )
    );
    public static final List<Menu> menus = List.of(menu1, menu2, menu3, menu4, menu5);

    public static ItemsDTO getItems() {
        return new ItemsDTO(Map.of(
                "first course", 10.0,
                "second course", 20.0,
                "third course", 30.0
        ));
    }

    public static Menu getNewMenu() {
        return new Menu(
                null,
                LocalDate.now(),
                new Restaurant(1, "Вкусная История", "ул. Пушкинская, 17, Москва"),
                Map.of(
                        "first course", 10.0,
                        "second course", 20.0,
                        "third course", 30.0
                )
        );
    }
}
