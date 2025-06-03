package graduation.lunchvote.restaurant.web;

import graduation.lunchvote.AbstractControllerTest;
import graduation.lunchvote.common.util.JsonUtil;
import graduation.lunchvote.restaurant.model.Menu;
import graduation.lunchvote.restaurant.repository.MenuRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.stream.Stream;

import static graduation.lunchvote.restaurant.MenuTestData.*;
import static graduation.lunchvote.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static graduation.lunchvote.user.UserTestData.ADMIN_MAIL;
import static graduation.lunchvote.restaurant.web.AdminMenuController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuAdminControllerTest extends AbstractControllerTest {

    @Autowired
    private MenuRepository menuRepository;

    @ParameterizedTest
    @MethodSource("provideParamsForCreateOrUpdateMenu")
    @WithUserDetails(value = ADMIN_MAIL)
    void createOrUpdateMenu(String date) throws Exception {
        Menu newMenu = getNewMenu();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .param("date", date)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getItems())))
                .andExpect(status().isOk());

        Menu created = MENU_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenu.setId(newId);
        newMenu.setDate(LocalDate.parse(date));
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(menuRepository.getExisted(newId), newMenu);
    }

    public static Stream<Arguments> provideParamsForCreateOrUpdateMenu() {
        return Stream.of(
                Arguments.of(LocalDate.now().plusDays(1).toString()),
                Arguments.of(LocalDate.now().toString())
        );
    }


}