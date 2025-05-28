package graduation.lunchvote.restaurant.util;

import graduation.lunchvote.restaurant.model.Menu;
import graduation.lunchvote.restaurant.to.MenuTo;

import java.util.Collection;
import java.util.List;

public class MenuUtil {
    public static List<MenuTo> getTos(Collection<Menu> menus) {
        return menus.stream()
                .map(MenuUtil::createTo)
                .toList();
    }

    public static MenuTo createTo(Menu menu) {
        return new MenuTo(menu.getId(), menu.getDate(), menu.getRestaurant().getId(), menu.getItems());
    }
}