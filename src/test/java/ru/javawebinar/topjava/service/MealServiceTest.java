package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.MealTestData.*;

/**
 * Created by wwwtm on 12.04.2017.
 */
@ContextConfiguration(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class MealServiceTest
{
    static
    {
        //SLF4JBridgeHandler.install();

    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserService userService;

    @Autowired
    MealService mealService;

    @Autowired
    DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception
    {
        dbPopulator.execute();
    }

    @Test
    public void get() throws Exception
    {
        Meal actual = mealService.get(100002, USER_ID);
        Meal expected = new Meal(LocalDateTime.now(),
                "Завтрак пользователя", 1200);
        expected.setId(actual.getId());
        expected.setDateTime(actual.getDateTime());
        MATCHER.assertEquals(expected, actual);

    }

    @Test
    public void delete() throws Exception
    {
        int id = 100002;

        List<Meal> expected = USER_MEALS
                .stream()
                .filter(m -> m.getId() != id)
                .collect(Collectors.toList());

        mealService.delete(id, USER_ID);
        List<Meal> actual = mealService.getAll(USER_ID);
        MATCHER.assertCollectionEquals(expected, actual);
    }

    @Test
    public void deleteCascade() throws Exception
    {
        userService.delete(USER_ID);
        List<Meal> actual = mealService.getAll(USER_ID);
        List<Meal> expected = jdbcTemplate.query("SELECT * FROM meals WHERE user_id=?", ROW_MAPPER, USER_ID);
        MATCHER.assertCollectionEquals(expected, actual);
    }

    @Test
    public void getBetweenDates() throws Exception
    {
        LocalDate from = LocalDate.of(2017, 3, 1);
        LocalDate to = LocalDate.of(2017, 3, 30);

        List<Meal> actual = mealService.getBetweenDates(
                from,
                to,
                USER_ID);

        List<Meal> expected = USER_MEALS
                .stream()
                .filter(m -> DateTimeUtil.isBetween(m.getDateTime(), LocalDateTime.of(from, LocalTime.MIN), LocalDateTime.of(to, LocalTime.MAX)))
                .collect(Collectors.toList());

        MATCHER.assertCollectionEquals(expected, actual);
    }

    @Test
    public void getBetweenDateTimes() throws Exception
    {

        LocalTime fromTime = LocalTime.of(9, 00);
        LocalTime toTime = LocalTime.of(13, 00);
        LocalDateTime from = LocalDateTime.of(DateTimeUtil.MIN_DATE, fromTime);
        LocalDateTime to = LocalDateTime.of(DateTimeUtil.MAX_DATE, toTime);

        List<Meal> actual = mealService.getBetweenDateTimes(from, to, USER_ID);
        List<Meal> expected = USER_MEALS
                .stream()
                .filter(m -> DateTimeUtil.isBetween(m.getDateTime(), from, to))
                .collect(Collectors.toList());

        MATCHER.assertCollectionEquals(expected, actual);
    }

    @Test
    public void getAll() throws Exception
    {
        List<Meal> actual = mealService.getAll(USER_ID);
        List<Meal> expected = USER_MEALS;

        MATCHER.assertCollectionEquals(expected, actual);
    }

    @Test
    public void update() throws Exception
    {
        int id = 100002;
        Meal meal = new Meal(LocalDateTime.now(), "New meal", 2000);
        meal.setId(id);
        Meal updated = mealService.update(meal, USER_ID);
        List<Meal> actual = mealService.getAll(USER_ID);

        ArrayList<Meal> meals = new ArrayList<>(USER_MEALS);

        List<Meal> expected = meals.stream()
                .map(m -> {
                    if (m.getId() == id)
                        m = updated;
                    return m;
                })
                .sorted(COMPARATOR)
                .collect(Collectors.toList());

        MATCHER.assertCollectionEquals(expected, actual);

    }

    @Test
    public void save() throws Exception
    {
        Meal meal = new Meal(LocalDateTime.now(), "New meal", 2000);

        meal.setId(mealService.save(meal, USER_ID).getId());
        List<Meal> actual = mealService.getAll(USER_ID);
        ArrayList<Meal> meals = new ArrayList<>(USER_MEALS);
        meals.add(meal);

        List<Meal> expected = meals.stream()
                .sorted(COMPARATOR)
                .collect(Collectors.toList());

        MATCHER.assertCollectionEquals(expected, actual);
    }

    @Test(expected = NotFoundException.class)
    public void tryDeleteOtherUserMeal()
    {
        int USER_ID = 100000;
        int adminMeal = 100010;

        mealService.delete(adminMeal, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void tryGetOtherUserMeal()
    {
        int adminMeal = 100010;
        Meal meal = mealService.get(adminMeal, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void trySaveOtherUserMeal()
    {
        int adminMeal = 100010;
        Meal meal = ADMIN_MEALS.stream()
                .filter(m -> m.getId() == adminMeal)
                .findFirst()
                .orElse(null);
        mealService.update(meal, USER_ID);
    }

}