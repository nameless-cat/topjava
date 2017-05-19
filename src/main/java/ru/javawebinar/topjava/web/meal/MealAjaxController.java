package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by wwwtm on 19.05.2017.
 */
@RestController
@RequestMapping(MealAjaxController.BASE_URL)
public class MealAjaxController
        extends AbstractMealController
{
    static final String BASE_URL = "/ajax/profile/meals";

    @PostMapping
    public void createOrUpdate(@RequestParam(value = "id", required = false) Integer id,
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("dateTime") LocalDateTime dateTime,
                               @RequestParam("description") String description,
                               @RequestParam("calories") Integer calories)
            throws NotFoundException
    {
        Meal meal = new Meal(id, dateTime, description, calories);

        if (meal.isNew())
        {
            super.create(meal);
        }
        else
        {
            super.update(meal, id);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getAll()
    {
        return super.getAll();
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Integer id)
            throws NotFoundException
    {
        super.delete(id);
    }
}
