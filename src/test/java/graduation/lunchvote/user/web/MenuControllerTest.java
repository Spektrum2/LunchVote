package graduation.lunchvote.user.web;

import graduation.lunchvote.AbstractControllerTest;
import graduation.lunchvote.common.util.JsonUtil;
import graduation.lunchvote.user.model.Menu;
import graduation.lunchvote.user.repository.MenuRepository;
import org.junit.jupiter.api.Test;
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

import static graduation.lunchvote.user.MenuTestData.*;
import static graduation.lunchvote.user.RestaurantTestData.RESTAURANT1_ID;
import static graduation.lunchvote.user.UserTestData.ADMIN_MAIL;
import static graduation.lunchvote.user.UserTestData.USER_MAIL;
import static graduation.lunchvote.user.web.MenuController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menus));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + MENU1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu1));

    }

    @ParameterizedTest
    @MethodSource("provideParamsForCreateOrUpdateMenu")
    @WithUserDetails(value = ADMIN_MAIL)
    void createOrUpdateMenu(String date) throws Exception {
        Menu newMenu = getNewMenu();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                        .param("restaurantId",  String.valueOf(RESTAURANT1_ID))
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