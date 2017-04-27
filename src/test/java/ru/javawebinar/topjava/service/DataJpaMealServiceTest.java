package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

/**
 * Created by wwwtm on 26.04.2017.
 */
@ActiveProfiles(profiles = Profiles.DATA_JPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest
{
    @BeforeClass
    public static void setUp()
    {
        setLogClass(DataJpaMealServiceTest.class);
        clearResult();
    }

    @AfterClass
    public static void result() {
        printResult();
    }

    @Test
    public void withMealMustExtractUserAsWell() throws Exception
    {
        Meal meal = service.getWithUser(MEAL1_ID);
        UserTestData.MATCHER.assertEquals(USER, meal.getUser());
    }
}
