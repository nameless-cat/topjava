package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.dto.FilterObject;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.validation.Valid;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

/**
 * Created by wwwtm on 05.05.2017.
 */
@Controller
@RequestMapping(value = "/meals")
public class AppController
{
    private static final Logger LOG = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService mealService;

    @RequestMapping(method = RequestMethod.GET, params = "!action")
    public String getAll(Model model)
    {
        LOG.info("Run: {}-{}", getClass().getSimpleName(), "getAll()");
        int userId = AuthorizedUser.id();
        List<MealWithExceed> meals = MealsUtil.getWithExceeded(mealService.getAll(userId), AuthorizedUser.getCaloriesPerDay());
        model.addAttribute("meals", meals);
        return "meals";
    }

    @RequestMapping(method = RequestMethod.GET, params = "action=create")
    public String getBlankMeal(Model model)
    {
        LOG.info("Run: {}-{}", getClass().getSimpleName(), "createMeal()");
        model.addAttribute("meal", MealsUtil.blankMeal());
        return "meal";
    }

    @RequestMapping(method = RequestMethod.GET, params = "action=update")
    public String getExistedMealForUpdate(@RequestParam("id") int id, Model model) throws NotFoundException
    {
        LOG.info("Run: {}-{}", getClass().getSimpleName(), "getExistedMealForUpdate()");

        int userId = AuthorizedUser.id();
        Meal meal = mealService.get(id, userId);
        model.addAttribute("meal", meal);
        return "meal";
    }

    @RequestMapping(method = RequestMethod.GET, params = "action=delete")
    public String deleteMeal(@RequestParam("id") int id, Model model) throws NotFoundException
    {
        LOG.info("Run: {}-{}", getClass().getSimpleName(), "deleteMeal()");
        int userId = AuthorizedUser.id();
        mealService.delete(id, userId);
        model.addAttribute("meals",
                MealsUtil.getWithExceeded(mealService.getAll(userId), AuthorizedUser.getCaloriesPerDay()));
        return "meals";
    }

    @RequestMapping(method = RequestMethod.POST, params = "action=update")
    public String updateMeal(@Valid @ModelAttribute("meal") Meal meal, BindingResult bindingResult, Model model)
            throws IllegalArgumentException, NotFoundException
    {
        LOG.info("Run: {}-{}", getClass().getSimpleName(), "updateMeal()");

        if (meal.getId() == null)
        {
            LOG.debug("Failing meal edit process: id has null value");
            throw new IllegalArgumentException();
        }

        if (hasInvalidCriticalFields(bindingResult))
        {
            return "meal";
        }

        mealService.update(meal, AuthorizedUser.id());
        return "redirect:/meals";
    }


    @RequestMapping(method = RequestMethod.POST, params = "action=create")
    public String createMeal(@Valid @ModelAttribute("meal") Meal meal, BindingResult bindingResult, Model model)
            throws IllegalArgumentException
    {
        LOG.info("Run: {}-{}", getClass().getSimpleName(), "createMeal()");

        checkNew(meal);

        if (hasInvalidCriticalFields(bindingResult))
        {
            return "meal";
        }

        mealService.save(meal, AuthorizedUser.id());

        return "redirect:/meals";
    }

    @RequestMapping(method = RequestMethod.POST, params = "action=filter")
    public String filterMeals(@ModelAttribute("filterObject") FilterObject filterObject, BindingResult bindingResult, Model model)
            throws IllegalArgumentException
    {
        LOG.info("Run: {}-{}", getClass().getSimpleName(), "filterMeals()");

        if (bindingResult.hasErrors())
        {
            //toDo нужен подробный отчет по полям с ошибками
            LOG.debug("Filter request with illegal fields");
            throw new IllegalArgumentException();
        }

        if (ValidationUtil.filterFormIsEmpty(filterObject))
            return "redirect:/meals";

        List<MealWithExceed> meals = MealsUtil.getFilteredWithExceeded(
                mealService.getBetweenDates(
                        filterObject.getStartDate() != null ? filterObject.getStartDate() : DateTimeUtil.MIN_DATE,
                        filterObject.getEndDate() != null ? filterObject.getEndDate() : DateTimeUtil.MAX_DATE,
                        AuthorizedUser.id()
                ),
                filterObject.getStartTime() != null ? filterObject.getStartTime() : LocalTime.MIN,
                filterObject.getEndTime() != null ? filterObject.getEndTime() : LocalTime.MAX,
                AuthorizedUser.getCaloriesPerDay()
        );
        model.addAttribute("meals", meals);
        return "meals";
    }

    @RequestMapping(method = RequestMethod.POST, params = "!action")
    public String defaultPostTo404()
    {
        LOG.info("Run: {}-{}", getClass().getSimpleName(), "defaultPostTo404()");

        throw new NotFoundException("Page not found");
    }
}
