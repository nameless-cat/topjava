package ru.javawebinar.topjava.repository;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * GKislin
 * 15.09.2015.
 */
@Repository
public class InMemoryMealRepositoryImpl
        implements MealRepository
{
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private Map<Integer, Set<Integer>> mapUsersToMeals = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(1, meal));
        MealsUtil.MEALS.forEach(meal -> save(2, meal));
        MealsUtil.MEALS.forEach(meal -> save(4, meal));
    }

    @Override
    public Meal save(int userId, Meal meal)
    {
        if (meal.isNew())
        {
            meal.setId(counter.incrementAndGet());
        }
        repository.put(meal.getId(), meal);

        mapUsersToMeals.computeIfAbsent(userId, k -> new HashSet<>());
        mapUsersToMeals.get(userId).add(meal.getId());

        return meal;
    }

    @Override
    public boolean delete(int userId, int mealId)
    {
        if (!mapUsersToMeals.get(userId).contains(mealId))
            return false;

        mapUsersToMeals.get(userId).remove(mealId);

        return repository.remove(mealId) != null;
    }

    @Override
    public Meal get(int userId, int mealId)
    {
        if (!mapUsersToMeals.get(userId).contains(mealId))
            return null;

        return repository.get(mealId);
    }

    @Override
    public Collection<Meal> getAll(int userId)
    {
        if (mapUsersToMeals.get(userId) == null)
            return Collections.emptyList();

        return repository.keySet()
                .stream()
                .filter(k -> mapUsersToMeals.get(userId).contains(k))
                .map(k -> repository.get(k))
                .sorted((m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getFiltered(int userId, LocalDate fromDate, LocalDate toDate)
    {
        return getAll(userId)
                .stream()
                .filter(m -> DateTimeUtil.isBetween(m.getDate(), fromDate, toDate))
                .collect(Collectors.toList());
    }

    public static void main(String[] args)
    {
        InMemoryMealRepositoryImpl repository = new InMemoryMealRepositoryImpl();

        System.out.println(repository.getAll(5));
        System.out.println(repository.getAll(2));

        repository.save(5, new Meal(LocalDateTime.now(), "Еда", 200));
        boolean removed = repository.delete(2, 7);
        repository.delete(5, 7);

        System.out.println(removed);
        System.out.println(repository.getAll(5));
        System.out.println(repository.getAll(2));
    }
}

