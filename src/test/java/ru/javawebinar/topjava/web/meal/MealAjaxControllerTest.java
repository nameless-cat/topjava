package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

/**
 * Created by wwwtm on 19.05.2017.
 */

/**
 * @note MealsWE in method names means MealsWithExceed
 */
public class MealAjaxControllerTest
        extends AbstractControllerTest
{
    private static final String BASE_URL = MealAjaxController.BASE_URL;

    @Autowired
    MealService service;

    @Test
    public void createMealReturnStatusCode200AndEntityMustPersistInDB() throws Exception
    {
        Meal created = MealTestData.getBuilder()
                .withDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .withDescription("new text description")
                .withCalories(400)
                .withId(100010)
                .build();

        mockMvc.perform(post(BASE_URL)
                .param("dateTime", created.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .param("description", created.getDescription())
                .param("calories", String.valueOf(created.getCalories())))
                .andDo(print())
                .andExpect(status().isOk());

        MealTestData.MATCHER.assertCollectionEquals(Arrays.asList(created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1), service.getAll(USER_ID));
    }

    @Test
    public void getRequestMustRenderActualListOfMealsWE() throws Exception
    {
        mockMvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ModelMatcher.of(MealWithExceed.class).contentListMatcher(MEALS_WE));
    }

    @Test
    public void deleteMealMustRerurnStatusCode200AndMealMustBeRemovedFromDB() throws Exception
    {
        mockMvc.perform(delete(BASE_URL + "/" + String.valueOf(MEAL1_ID)))
                .andDo(print())
                .andExpect(status().isOk());

        MealTestData.MATCHER.assertCollectionEquals(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2), service.getAll(USER_ID));
    }

    @Test
    public void getFilteredMealsMustRenderCorrectListOfMealsWE() throws Exception
    {
        mockMvc.perform(get(BASE_URL)
                .param("endDate", "2015-05-30"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MealTestData.MATCHER_WITH_EXCEED.contentListMatcher(Arrays.asList(MEAL_WE3, MEAL_WE2, MEAL_WE1)));
    }
}
