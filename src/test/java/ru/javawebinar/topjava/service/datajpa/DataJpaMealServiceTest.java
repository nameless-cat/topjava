package ru.javawebinar.topjava.service.datajpa;

import net.ttddyy.dsproxy.QueryCount;
import net.ttddyy.dsproxy.QueryCountHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.testng.Assert;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractJpaMealServiceTest;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends AbstractJpaMealServiceTest
{
    @Test
    public void testGetWithUser() throws Exception {
        Meal adminMeal = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        MATCHER.assertEquals(ADMIN_MEAL1, adminMeal);
        UserTestData.MATCHER.assertEquals(UserTestData.ADMIN, adminMeal.getUser());
        setExpectedQueries(2);
    }

    @Test(expected = NotFoundException.class)
    public void testGetWithUserNotFound() throws Exception {
        service.getWithUser(MEAL1_ID, ADMIN_ID);
    }

    @Override
    public void testDelete() throws Exception
    {
        super.testDelete();
        setExpectedQueries(2);
    }

    @Override
    public void testSave() throws Exception
    {
        super.testSave();
        setExpectedQueries(3);
    }

    @Override
    public void testGet() throws Exception
    {
        super.testGet();
        setExpectedQueries(1);
    }

    @Override
    public void testUpdate() throws Exception
    {
        super.testUpdate();
        setExpectedQueries(3);
    }

    @Override
    public void testGetAll() throws Exception
    {
        super.testGetAll();
        setExpectedQueries(1);
    }

    @Override
    public void testGetBetween() throws Exception
    {
        super.testGetBetween();
        setExpectedQueries(1);
    }
}
