package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.MATCHER;
import static ru.javawebinar.topjava.UserTestData.NEW_TEST_USER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

/**
 * Created by wwwtm on 26.04.2017.
 */
@ActiveProfiles(Profiles.DATA_JPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest
{
    @Test
    public void getUserWithMealsAsWell() throws Exception
    {
        User user = service.getWithMeals(USER_ID);
        List<Meal> expected = Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
        List<Meal> actual = user.getMeals();
        Assert.assertEquals(expected.size(), actual.size());
        MealTestData.MATCHER.assertCollectionEquals(expected, actual);
    }

    @Test
    public void userMustHaveEmptyMealsListIfHeHaventAny() throws Exception
    {
        User u = service.save(NEW_TEST_USER);
        MealTestData.MATCHER.assertCollectionEquals(Collections.emptyList(), service.getWithMeals(u.getId()).getMeals());
    }
}
