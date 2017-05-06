package ru.javawebinar.topjava.service.mock;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceImpl;

/**
 * Created by wwwtm on 05.05.2017.
 */
public class MockMealServiceBeanHolder
{
    MealService mockMealService()
    {
        return Mockito.mock(MealServiceImpl.class);
    }
}
