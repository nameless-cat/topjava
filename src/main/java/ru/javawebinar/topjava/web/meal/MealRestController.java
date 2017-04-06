package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {

    private MealService service;
    private String NOT_FOUND = "Record not found";

    public void delete(int mealId)
            throws NotFoundException
    {
        if (!service.delete(AuthorizedUser.id(), mealId))
            throw new NotFoundException(NOT_FOUND);
    }

    public void update(Meal meal)
            throws NotFoundException
    {
        if (!service.update(AuthorizedUser.id(), meal))
            throw new NotFoundException(NOT_FOUND);
    }

    public List<MealWithExceed> getMeals()
    {
        return service.getMeals(AuthorizedUser.id());
    }

    public Meal get(int mealId)
            throws NotFoundException
    {
        Meal meal = service.get(AuthorizedUser.id(), mealId);

        if (meal == null)
            throw new NotFoundException(NOT_FOUND);

        return meal;
    }

    public Meal getBlank()
    {
        return service.getBlank();
    }

    public List<MealWithExceed> getFilteredMeals(LocalDate fDate, LocalDate tDate, LocalTime fTime, LocalTime tTime)
            throws IllegalArgumentException
    {
        LocalDate fromDate = fDate != null ? fDate : LocalDate.MIN;
        LocalDate toDate = tDate != null ? tDate : LocalDate.MAX;
        LocalTime fromTime = fTime != null ? fTime : LocalTime.MIN;
        LocalTime toTime = tTime != null ? tTime : LocalTime.MAX;

        if (fromDate.isAfter(toDate) || fromTime.isAfter(toTime))
            throw new IllegalArgumentException();

        return service.getFilteredMeals(AuthorizedUser.id(), fromDate, toDate, fromTime, toTime);
    }
}