package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepositoryImpl
        implements MealRepository
{

    @Autowired
    private CrudMealRepository crudRepository;

    @Autowired
    private CrudUserRepository userRepository;

    @Override
    public Meal save(Meal meal, int userId)
    {
        meal.setUser(userRepository.findOne(userId));

        if (meal.isNew())
        {
            return crudRepository.save(meal);
        } else
        {
            return (crudRepository.update(meal, userId) != 0) ? meal : null;
        }
    }

    @Override
    public boolean delete(int id, int userId)
    {
        return crudRepository.deleteByIdAndUserId(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId)
    {
        return crudRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId)
    {
        return crudRepository.findByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId)
    {
        return crudRepository.findByUserIdAndDateTimeBetweenOrderByDateTimeDesc(userId, startDate, endDate);
    }
}
