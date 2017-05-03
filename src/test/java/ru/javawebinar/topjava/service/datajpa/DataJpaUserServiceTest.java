package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractJpaUserServiceTest;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractJpaUserServiceTest
{
    @Test
    public void testGetWithMeals() throws Exception {
        User user = service.getWithMeals(USER_ID);
        MATCHER.assertEquals(USER, user);
        MealTestData.MATCHER.assertCollectionEquals(MealTestData.MEALS, user.getMeals());
        setExpectedQueries(1);
    }

    @Test(expected = NotFoundException.class)
    public void testGetWithMealsNotFound() throws Exception {
        service.getWithMeals(1);
        setExpectedQueries(1);
    }

    @Override
    public void testSave() throws Exception
    {
        // toDO 5 запросов в базу! Можно ли сохранить или обновить пользователя одним SQL запросом?
        super.testSave();
        setExpectedQueries(5);
    }

        @Override
    public void testDelete() throws Exception
    {
        super.testDelete();
        setExpectedQueries(3);
    }

        @Override
    public void testGet() throws Exception
    {
        super.testGet();
        setExpectedQueries(2);
    }

    @Override
    public void testGetByEmail() throws Exception
    {
        super.testGetByEmail();
        setExpectedQueries(1);
    }

    @Override
    public void testGetAll() throws Exception
    {
        super.testGetAll();
        setExpectedQueries(2);
    }

    @Override
    public void testUpdate() throws Exception
    {
        // toDO 5 запросов в базу! Можно ли сохранить или обновить пользователя одним SQL запросом?
        super.testUpdate();
        setExpectedQueries(5);
    }
}