package graduation.lunchvote.restaurant;

import graduation.lunchvote.MatcherFactory;
import graduation.lunchvote.restaurant.model.Menu;
import graduation.lunchvote.restaurant.model.MenuItem;
import graduation.lunchvote.restaurant.model.Restaurant;
import graduation.lunchvote.restaurant.to.MenuRequest;
import graduation.lunchvote.restaurant.to.MenuTo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant");
    public static final MatcherFactory.Matcher<MenuTo> MENU_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuTo.class);
    public static final int MENU1_ID = 1;
    public static final MenuTo menu1 = new MenuTo(
            MENU1_ID,
            LocalDate.now(),
            1,
            List.of(
                    new MenuItem("Салат Цезарь с курицей", new BigDecimal("350.00")),
                    new MenuItem("Борщ с говядиной", new BigDecimal("250.00")),
                    new MenuItem("Стейк из свинины с картофельным пюре", new BigDecimal("500.00")),
                    new MenuItem("Компот из ягод", new BigDecimal("100.00")),
                    new MenuItem("Пирожное Наполеон", new BigDecimal("150.00"))
            )
    );
    public static final MenuTo menu2 = new MenuTo(
            MENU1_ID + 1,
            LocalDate.now(),
            2,
            List.of(
                    new MenuItem("Греческий салат", new BigDecimal("300.00")),
                    new MenuItem("Суп с грибами и вермишелью", new BigDecimal("220.00")),
                    new MenuItem("Куриное филе в сливочно-грибном соусе с рисом", new BigDecimal("450.00")),
                    new MenuItem("Морс из клюквы", new BigDecimal("90.00")),
                    new MenuItem("Чизкейк", new BigDecimal("180.00"))
            )
    );
    public static final MenuTo menu3 = new MenuTo(
            MENU1_ID + 2,
            LocalDate.now(),
            3,
            List.of(
                    new MenuItem("Салат Оливье", new BigDecimal("320.00")),
                    new MenuItem("Крем-суп из тыквы", new BigDecimal("200.00")),
                    new MenuItem("Филе лосося на гриле с овощами", new BigDecimal("600.00")),
                    new MenuItem("Компот из сухофруктов", new BigDecimal("120.00")),
                    new MenuItem("Эклер с кремом", new BigDecimal("130.00"))
            )
    );
    public static final MenuTo menu4 = new MenuTo(
            MENU1_ID + 3,
            LocalDate.now(),
            4,
            List.of(
                    new MenuItem("Салат Винегрет", new BigDecimal("280.00")),
                    new MenuItem("Щи с говядиной", new BigDecimal("230.00")),
                    new MenuItem("Телятина с гречневой кашей и грибами", new BigDecimal("520.00")),
                    new MenuItem("Кисель", new BigDecimal("100.00")),
                    new MenuItem("Медовик", new BigDecimal("160.00"))
            )
    );
    public static final MenuTo menu5 = new MenuTo(
            MENU1_ID + 4,
            LocalDate.now(),
            5,
            List.of(
                    new MenuItem("Салат Капрезе", new BigDecimal("310.00")),
                    new MenuItem("Уха из красной рыбы", new BigDecimal("270.00")),
                    new MenuItem("Запеченная куриная грудка с овощами", new BigDecimal("440.00")),
                    new MenuItem("Лимонад", new BigDecimal("90.00")),
                    new MenuItem("Яблочный штрудель", new BigDecimal("170.00"))
            )
    );
    public static final List<MenuTo> menus = List.of(menu1, menu2, menu3, menu4, menu5);

    public static MenuRequest getItems() {
        return new MenuRequest(List.of(
                new MenuItem("first course", new BigDecimal("10.0")),
                new MenuItem("second course", new BigDecimal("20.0")),
                new MenuItem("third course", new BigDecimal("30.0"))
        ));
    }

    public static Menu getNewMenu() {
        return new Menu(
                null,
                LocalDate.now(),
                new Restaurant(1, "Вкусная История", "ул. Пушкинская, 17, Москва"),
                List.of(
                        new MenuItem("first course", new BigDecimal("10.0")),
                        new MenuItem("second course", new BigDecimal("20.0")),
                        new MenuItem("third course", new BigDecimal("30.0"))
                )
        );
    }
}
