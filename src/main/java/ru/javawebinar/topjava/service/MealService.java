package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MealService {
    boolean delete(int userId, int mealId);

    boolean update(int userId, Meal meal);

    List<MealWithExceed> getMeals(int userId);

    Meal get(int userId, int mealId);

    Meal getBlank();

    List<MealWithExceed> getFilteredMeals(int id, LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime);
}