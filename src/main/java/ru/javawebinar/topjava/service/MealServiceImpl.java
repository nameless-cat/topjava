package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealServiceImpl
        implements MealService
{
    private MealRepository repository;
    private UserRepository users;

    @Override
    public boolean delete(int userId, int mealId)
    {
        return repository.delete(userId, mealId);
    }

    @Override
    public boolean update(int userId, Meal meal)
    {
        return repository.save(userId, meal) != null;
    }

    @Override
    public List<MealWithExceed> getMeals(int userId)
    {
        return MealsUtil.getWithExceeded(repository.getAll(userId), users.get(userId).getCaloriesPerDay());
    }

    @Override
    public Meal get(int userId, int mealId)
    {
        if (mealId < 1)
            return null;

        return repository.get(userId, mealId);
    }

    @Override
    public Meal getBlank()
    {
        return MealsUtil.getBlank();
    }

    @Override
    public List<MealWithExceed> getFilteredMeals(int userId, LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime)
            throws IllegalArgumentException
    {
        return MealsUtil.getWithExceeded(repository.getFiltered(userId, fromDate, toDate), users.get(userId).getCaloriesPerDay())
                .stream()
                .filter(k -> DateTimeUtil.isBetween(k.getTime(), fromTime, toTime))
                .collect(Collectors.toList());
    }
}