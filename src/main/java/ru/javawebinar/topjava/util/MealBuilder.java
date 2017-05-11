package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;

/**
 * Created by wwwtm on 10.05.2017.
 */
public class MealBuilder
{
    private Meal meal;

    public MealBuilder()
    {
        this.meal = new Meal();
    }

    public MealBuilder withId(Integer mealId)
    {
        meal.setId(mealId);
        return this;
    }

    public MealBuilder withUser(User user)
    {
        meal.setUser(user);
        return this;
    }


    public MealBuilder withDateTime(LocalDateTime localDateTime)
    {
        meal.setDateTime(localDateTime);
        return this;
    }


    public MealBuilder withDescription(String description)
    {
        meal.setDescription(description);
        return this;
    }

    public MealBuilder withCalories(int calories)
    {
        meal.setCalories(calories);
        return this;
    }

    public Meal build()
    {
        return meal;
    }
}
