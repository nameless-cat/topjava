package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping(MealRestController.MEALS_REST_URL)
public class MealRestController
        extends AbstractMealController
{
    static final String MEALS_REST_URL = "/rest/meals";

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id, HttpServletResponse response)
            throws NotFoundException, IOException
    {
        super.delete(id);
        response.sendRedirect(MEALS_REST_URL);
    }


    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Valid @RequestBody Meal meal, @PathVariable int id, HttpServletResponse response)
            throws NotFoundException, IllegalArgumentException, IOException
    {
        super.update(meal, id);
        response.sendRedirect(MEALS_REST_URL);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Valid @RequestBody Meal meal, HttpServletResponse response)
            throws IllegalArgumentException, IOException
    {
        super.create(meal);
        response.sendRedirect(MEALS_REST_URL);
    }

    @GetMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public Meal getForCreate()
    {
        return new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), "", 1000);
    }

    @GetMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Meal getForUpdate(@PathVariable int id)
        throws NotFoundException
    {
        return super.get(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getAllOrFiltered(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(value = "startDateTime", required = false) LocalDateTime start,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(value = "endDateTime", required = false) LocalDateTime end
    )
    {
        if (start == null & end == null)
        {
            return super.getAll();
        }

        return super.getBetween(
                start != null ? start.toLocalDate() : null,
                start != null ? start.toLocalTime() : null,
                end != null ? end.toLocalDate() : null,
                end != null ? end.toLocalTime() : null);
    }
}