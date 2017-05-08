package ru.javawebinar.topjava.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

/**
 * Created by wwwtm on 05.05.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-mvc.xml", "classpath:spring/app-controller-test-mock.xml", "classpath:spring/spring-db.xml"})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@WebAppConfiguration
@ActiveProfiles(value = {Profiles.POSTGRES_DB, Profiles.DATAJPA})
public class AppControllerTest
{
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MealService mockMealService;

    @Before
    public void setUp()
    {
        Mockito.reset(mockMealService);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

@Test
    public void getAllUserMealsAddToModelAndRenderToMealsView() throws Exception
    {
        when(mockMealService.getAll(USER_ID)).thenReturn(MEALS);

        mockMvc.perform(get("/meals"))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(6)))
                .andExpect(model().attribute("meals", MEALS_WE));

        verify(mockMealService, times(1)).getAll(USER_ID);
        verifyNoMoreInteractions(mockMealService);
    }

    @Test
    public void processGetRequestWithParamActionCreate() throws Exception
    {
        Meal blankMeal = MealsUtil.blankMeal();

        mockMvc.perform(get("/meals")
                .param("action", "create"))
                .andExpect(status().isOk())
                .andExpect(view().name("meal"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meal.jsp"))
                .andExpect(model().attributeExists("meal"))
                .andExpect(model().attribute("meal", hasProperty("description", is(blankMeal.getDescription()))))
                .andExpect(model().attribute("meal", hasProperty("calories", is(blankMeal.getCalories()))));

        verifyZeroInteractions(mockMealService);
    }

    @Test
    public void processGetRequestWithParamActionUpdate() throws Exception
    {
        when(mockMealService.get(MEAL1_ID, USER_ID)).thenReturn(MEAL1);

        mockMvc.perform(get("/meals")
                .param("action", "update")
                .param("id", String.valueOf(MEAL1_ID)))
                .andExpect(status().isOk())
                .andExpect(view().name("meal"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meal.jsp"))
                .andExpect(model().attributeExists("meal"))
                .andExpect(model().attribute("meal", hasProperty("id", is(MEAL1_ID))));

        verify(mockMealService, times(1)).get(MEAL1_ID, USER_ID);
        verifyNoMoreInteractions(mockMealService);
    }

    @Test
    public void processGetRequestWithParamActionDelete() throws Exception
    {
        List<MealWithExceed> expected = new ArrayList<>(MEALS_WE);
        expected.remove(MEAL_WE1);
        when(mockMealService.getAll(USER_ID)).thenReturn(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2));

        mockMvc.perform(get("/meals")
                .param("action", "delete")
                .param("id", String.valueOf(MEAL1_ID)))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(5)))
                .andExpect(model().attribute("meals", expected));

        verify(mockMealService, times(1)).delete(MEAL1_ID, USER_ID);
        verify(mockMealService, times(1)).getAll(USER_ID);
        verifyNoMoreInteractions(mockMealService);
    }

    @Test
    public void updateMealThroughPostRequestMustRenderListOfUserMeals() throws Exception
    {
        Meal updated = new Meal();
        updated.setId(MEAL1_ID);
        updated.setDateTime(LocalDateTime.now());
        updated.setDescription("updated description");
        updated.setCalories(125);

        mockMvc.perform(post("/meals")
                .param("action", "update")
                .param("id", String.valueOf(MEAL1_ID))
                .param("dateTime", String.valueOf(updated.getDateTime()))
                .param("description", updated.getDescription())
                .param("calories", String.valueOf(updated.getCalories())))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/meals"))
                .andExpect(redirectedUrl("/meals"));

        verify(mockMealService, times(1)).update(updated, USER_ID);
        verifyNoMoreInteractions(mockMealService);
    }

    @Test
    public void createMealThroughPostRequestMustRenderListOfUserMeals() throws Exception
    {
        Meal meal = MealTestData.getCreated();

        mockMvc.perform(post("/meals")
                .param("action", "create")
                .param("id", String.valueOf(meal.getId()))
                .param("dateTime", String.valueOf(meal.getDateTime()))
                .param("description", meal.getDescription())
                .param("calories", String.valueOf(meal.getCalories())))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/meals"))
                .andExpect(redirectedUrl("/meals"));
    }

    @Test
    public void createViaPostRequestWithWrongDescriptionMustReturnUserToForm() throws Exception
    {
        mockMvc.perform(post("/meals")
                .param("action", "create")
                .param("calories", "200")
                .param("description", "")
                .param("dateTime", "2014-12-03T21:00"))
                .andExpect(status().isOk())
                .andExpect(view().name("meal"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meal.jsp"));
    }

    @Test
    public void createViaPostRequestWithWrongCaloriesMustReturnUserToForm() throws Exception
    {
        mockMvc.perform(post("/meals")
                .param("action", "create")
                .param("calories", "+")
                .param("description", "new description")
                .param("dateTime", "2014-12-03T21:00"))
                .andExpect(status().isOk())
                .andExpect(view().name("meal"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meal.jsp"));
    }

    @Test
    public void createViaPostRequestWithWrongDateTimeMustReturnUserToForm() throws Exception
    {
        mockMvc.perform(post("/meals")
                .param("action", "create")
                .param("calories", "200")
                .param("description", "new description")
                .param("dateTime", "+"))
                .andExpect(status().isOk())
                .andExpect(view().name("meal"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meal.jsp"));
    }

    @Test
    public void updateViaPostRequestWithWrongDescriptionMustReturnUserToForm() throws Exception
    {
        mockMvc.perform(post("/meals")
                .param("action", "update")
                .param("id", "100002")
                .param("calories", "200")
                .param("description", "")
                .param("dateTime", "2014-12-03T21:00"))
                .andExpect(status().isOk())
                .andExpect(view().name("meal"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meal.jsp"));
    }

    @Test
    public void updateViaPostRequestWithWrongDateTimeMustReturnUserToForm() throws Exception
    {
        mockMvc.perform(post("/meals")
                .param("action", "update")
                .param("id", "100002")
                .param("calories", "200")
                .param("description", "new description")
                .param("dateTime", "+"))
                .andExpect(status().isOk())
                .andExpect(view().name("meal"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meal.jsp"));
    }

    @Test
    public void updateViaPostRequestWithWrongCaloriesMustReturnUserToForm() throws Exception
    {
        mockMvc.perform(post("/meals")
                .param("action", "update")
                .param("id", "100002")
                .param("calories", "+")
                .param("description", "new description")
                .param("dateTime", "2014-12-03T21:00"))
                .andExpect(status().isOk())
                .andExpect(view().name("meal"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meal.jsp"));
    }

    @Test
    public void updateViaPostRequestWithNullIdMustRenderBadRequest() throws Exception
    {
        mockMvc.perform(post("/meals")
                .param("action", "update"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error/400"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/400.jsp"));
    }

    @Test
    public void requestWithActionFilterMustRenderListOfMealsInRange() throws Exception
    {
        LocalDate startDate = LocalDate.of(2015, Month.MAY, 29);
        LocalDate endDate = LocalDate.of(2015, Month.MAY, 30);
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(14, 0);

        when(mockMealService.getBetweenDates(startDate, endDate, USER_ID)).thenReturn(Arrays.asList(MEAL3, MEAL2, MEAL1));

        mockMvc.perform(post("/meals")
                .param("action", "filter")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .param("startTime", startTime.toString())
                .param("endTime", endTime.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(2)))
                .andExpect(model().attribute("meals", Arrays.asList(MEAL_WE2, MEAL_WE1)));

        verify(mockMealService, times(1)).getBetweenDates(startDate, endDate, USER_ID);
        verifyNoMoreInteractions(mockMealService);
    }

    @Test
    public void filterRequestWithLocalDateOnly() throws Exception
    {
        LocalDate startDate = LocalDate.of(2015, Month.MAY, 29);
        LocalDate endDate = LocalDate.of(2015, Month.MAY, 30);

        when(mockMealService.getBetweenDates(startDate, endDate, USER_ID))
                .thenReturn(Arrays.asList(MEAL3, MEAL2, MEAL1));

        mockMvc.perform(post("/meals")
                .param("action", "filter")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(3)))
                .andExpect(model().attribute("meals", Arrays.asList(MEAL_WE3, MEAL_WE2, MEAL_WE1)));

        verify(mockMealService, times(1))
                .getBetweenDates(startDate, endDate, USER_ID);
        verifyNoMoreInteractions(mockMealService);
    }

    @Test
    public void filterRequestWithLocalTimeOnly() throws Exception
    {
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(14, 0);

        when(mockMealService.getBetweenDates(DateTimeUtil.MIN_DATE, DateTimeUtil.MAX_DATE, USER_ID))
                .thenReturn(MEALS);

        mockMvc.perform(post("/meals")
                .param("action", "filter")
                .param("startTime", startTime.toString())
                .param("endTime", endTime.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(4)))
                .andExpect(model().attribute("meals", Arrays.asList(MEAL_WE5, MEAL_WE4, MEAL_WE2, MEAL_WE1)));

        verify(mockMealService, times(1))
                .getBetweenDates(DateTimeUtil.MIN_DATE, DateTimeUtil.MAX_DATE, USER_ID);
        verifyNoMoreInteractions(mockMealService);
    }

    @Test
    public void filterRequestWithStartTimeAndStartDate() throws Exception
    {
        LocalDate startDate = LocalDate.of(2015, Month.MAY, 29);
        LocalTime startTime = LocalTime.of(14, 0);

        when(mockMealService.getBetweenDates(startDate, DateTimeUtil.MAX_DATE, USER_ID))
                .thenReturn(MEALS);

        mockMvc.perform(post("/meals")
                .param("action", "filter")
                .param("startDate", startDate.toString())
                .param("startTime", startTime.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(2)))
                .andExpect(model().attribute("meals", Arrays.asList(MEAL_WE6, MEAL_WE3)));

        verify(mockMealService, times(1))
                .getBetweenDates(startDate, DateTimeUtil.MAX_DATE, USER_ID);
        verifyNoMoreInteractions(mockMealService);
    }

    @Test
    public void filterRequestWithEndTimeAndEndDate() throws Exception
    {
        LocalDate endDate = LocalDate.of(2015, Month.MAY, 30);
        LocalTime endTime = LocalTime.of(14, 0);

        when(mockMealService.getBetweenDates(DateTimeUtil.MIN_DATE, endDate, USER_ID))
                .thenReturn(Arrays.asList(MEAL3, MEAL2, MEAL1));

        mockMvc.perform(post("/meals")
                .param("action", "filter")
                .param("endDate", endDate.toString())
                .param("endTime", endTime.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(2)))
                .andExpect(model().attribute("meals", Arrays.asList(MEAL_WE2, MEAL_WE1)));

        verify(mockMealService, times(1))
                .getBetweenDates(DateTimeUtil.MIN_DATE, endDate, USER_ID);
        verifyNoMoreInteractions(mockMealService);
    }

    @Test
    public void filterRequestWithStartDateOnly() throws Exception
    {
        LocalDate startDate = LocalDate.of(2015, Month.MAY, 29);

        when(mockMealService.getBetweenDates(startDate, DateTimeUtil.MAX_DATE, USER_ID))
                .thenReturn(MEALS);

        mockMvc.perform(post("/meals")
                .param("action", "filter")
                .param("startDate", startDate.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(6)))
                .andExpect(model().attribute("meals", MEALS_WE));

        verify(mockMealService, times(1))
                .getBetweenDates(startDate, DateTimeUtil.MAX_DATE, USER_ID);
        verifyNoMoreInteractions(mockMealService);
    }

    @Test
    public void filterRequestWithStartTimeOnly() throws Exception
    {
        LocalTime startTime = LocalTime.of(9, 0);

        when(mockMealService.getBetweenDates(DateTimeUtil.MIN_DATE, DateTimeUtil.MAX_DATE, USER_ID))
                .thenReturn(MEALS);

        mockMvc.perform(post("/meals")
                .param("action", "filter")
                .param("startTime", startTime.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(6)))
                .andExpect(model().attribute("meals", MEALS_WE));

        verify(mockMealService, times(1))
                .getBetweenDates(DateTimeUtil.MIN_DATE, DateTimeUtil.MAX_DATE, USER_ID);
        verifyNoMoreInteractions(mockMealService);
    }

    @Test
    public void filterRequestWithEndDateOnly() throws Exception
    {
        LocalDate endDate = LocalDate.of(2015, Month.MAY, 30);

        when(mockMealService.getBetweenDates(DateTimeUtil.MIN_DATE, endDate, USER_ID))
                .thenReturn(Arrays.asList(MEAL3, MEAL2, MEAL1));

        mockMvc.perform(post("/meals")
                .param("action", "filter")
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(3)))
                .andExpect(model().attribute("meals", Arrays.asList(MEAL_WE3, MEAL_WE2, MEAL_WE1)));

        verify(mockMealService, times(1))
                .getBetweenDates(DateTimeUtil.MIN_DATE, endDate, USER_ID);
        verifyNoMoreInteractions(mockMealService);
    }

    @Test
    public void filterRequestWithEndTimeOnly() throws Exception
    {
        LocalTime endTime = LocalTime.of(14, 0);

        when(mockMealService.getBetweenDates(DateTimeUtil.MIN_DATE, DateTimeUtil.MAX_DATE, USER_ID))
                .thenReturn(MEALS);

        mockMvc.perform(post("/meals")
                .param("action", "filter")
                .param("endTime", endTime.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(4)))
                .andExpect(model().attribute("meals", Arrays.asList(MEAL_WE5, MEAL_WE4, MEAL_WE2, MEAL_WE1)));

        verify(mockMealService, times(1))
                .getBetweenDates(DateTimeUtil.MIN_DATE, DateTimeUtil.MAX_DATE, USER_ID);
        verifyNoMoreInteractions(mockMealService);
    }

    @Test
    public void filterRequestWithoutActionParameterMustThrowNotFoundException() throws Exception
    {
        LocalTime endTime = LocalTime.of(14, 0);

        mockMvc.perform(post("/meals")
                .param("endTime", endTime.toString()))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    public void filterRequestWithIllegalStartDate() throws Exception
    {
        LocalDate endDate = LocalDate.of(2015, Month.MAY, 30);
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(14, 0);

        when(mockMealService.getBetweenDates(DateTimeUtil.MIN_DATE, endDate, USER_ID))
                .thenReturn(Arrays.asList(MEAL3, MEAL2, MEAL1));

        mockMvc.perform(post("/meals")
                .param("action", "filter")
                .param("startDate", "123")
                .param("endDate", endDate.toString())
                .param("startTime", startTime.toString())
                .param("endTime", endTime.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(2)))
                .andExpect(model().attribute("meals", Arrays.asList(MEAL_WE2, MEAL_WE1)));

        verify(mockMealService, times(1))
                .getBetweenDates(DateTimeUtil.MIN_DATE, endDate, USER_ID);
        verifyNoMoreInteractions(mockMealService);
    }

    //toDO need implementation
    @Test
    public void filterRequestWithIllegalEndDate() throws Exception
    {

    }

    //toDO need implementation
    @Test
    public void filterRequestWithIllegalStartTime() throws Exception
    {

    }

    //toDO need implementation
    @Test
    public void filterRequestWithIllegalEndTime() throws Exception
    {
    }

    @Test
    public void filterRequestWithEmptyFormFieldsMustRedirectToMeals() throws Exception
    {
        mockMvc.perform(post("/meals")
                .param("action", "filter"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/meals"))
                .andExpect(redirectedUrl("/meals"));
    }

    @Test
    public void updateMealWithWrongIdMustThrowNotFoundException() throws Exception
    {
        when(mockMealService.get(ADMIN_MEAL_ID, USER_ID)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/meals")
                .param("action", "update")
                .param("id", String.valueOf(ADMIN_MEAL_ID)))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));

        verify(mockMealService, times(1)).get(ADMIN_MEAL_ID, USER_ID);
        verifyNoMoreInteractions(mockMealService);
    }

    @Test
    public void sendingWrongActionParamResultsInBadRequest() throws Exception
    {
        mockMvc.perform(get("/meals")
                .param("action", "wrongValue"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteMealWithWrongIdMustThrowNotFoundException() throws Exception
    {

        when(mockMealService.delete(ADMIN_MEAL_ID, USER_ID)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/meals")
                .param("action", "delete")
                .param("id", String.valueOf(ADMIN_MEAL_ID)))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));

        verify(mockMealService, times(1)).delete(ADMIN_MEAL_ID, USER_ID);
        verifyNoMoreInteractions(mockMealService);
    }
}
