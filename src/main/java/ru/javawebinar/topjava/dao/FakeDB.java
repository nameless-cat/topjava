package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import ru.javawebinar.topjava.exception.DBEntityNotFoundException;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by wwwtm on 27.03.2017.
 */
public class FakeDB
{
    private static final Logger LOG = getLogger(FakeDB.class);
    private static ConcurrentLinkedQueue<Meal> mealList = new ConcurrentLinkedQueue<>();
    private static FakeDB instance;

    private FakeDB()
    {

        if (mealList.isEmpty())
        {
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 30, 10, 0), "Завтрак", 500));
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 30, 13, 0), "Обед", 1000));
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 30, 20, 0), "Ужин", 500));
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 31, 10, 0), "Завтрак", 1000));
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 31, 13, 0), "Обед", 500));
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 31, 20, 0), "Ужин", 510));
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 1, 10, 0), "Завтрак", 700));
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 1, 13, 0), "Обед", 800));
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 1, 20, 0), "Ужин", 310));
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 2, 10, 0), "Завтрак", 1000));
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 2, 13, 0), "Обед", 400));
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 2, 20, 0), "Ужин", 800));
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 3, 10, 0), "Завтрак", 1500));
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 3, 13, 0), "Обед", 800));
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 3, 20, 0), "Ужин", 710));
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 4, 10, 0), "Завтрак", 900));
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 4, 13, 0), "Обед", 1500));
            mealList.add(new Meal(LocalDateTime.of(2016, Month.MAY, 4, 20, 0), "Ужин", 1510));
        }
    }

    public ConcurrentLinkedQueue<Meal> getMealList()
    {
        return mealList;
    }

    public void store(Meal meal)
    {
        boolean added = false;

        for (Meal m : mealList)
        {
            if (m.getId() == meal.getId())
            {
                m.setDescription(meal.getDescription());
                m.setDateTime(meal.getDateTime());
                m.setCalories(meal.getCalories());
                added = true;
            }
        }

        if (!added)
            mealList.add(meal);
    }

    public static FakeDB getInstance()
    {
        if (instance == null)
            instance = new FakeDB();

        return instance;
    }

    public void delete(long id)
            throws DBEntityNotFoundException
    {
        if (!mealList.remove(getById(id)))
            throw new DBEntityNotFoundException();
    }

    public Meal getById(long id)
            throws DBEntityNotFoundException
    {
        Meal meal = null;

        for (Meal m : mealList)
        {
            if (m.getId() == id)
            {
                meal = m;
                break;
            }
        }

        if (meal == null)
            throw new DBEntityNotFoundException();

        return meal;
    }
}
