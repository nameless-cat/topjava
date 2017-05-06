package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
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
    @Qualifier("mealServiceImpl")
    private MealService mealService;

    @RequestMapping(method = RequestMethod.GET, params = "!action")
    public String getAll(Model model)
    {
        LOG.info("Run: {} {}", getClass().getSimpleName(), "getAll()");
        int userId = AuthorizedUser.id();
        List<MealWithExceed> meals = MealsUtil.getWithExceeded(mealService.getAll(userId), AuthorizedUser.getCaloriesPerDay());
        model.addAttribute("meals", meals);
        return "meals";
    }

    @RequestMapping(method = RequestMethod.GET, params = "action=create")
    public String getBlankMeal(Model model)
    {
        LOG.info("Run: {} {}", getClass().getSimpleName(), "createMeal()");
        model.addAttribute("meal", MealsUtil.blankMeal());
        return "meal";
    }

    @RequestMapping(method = RequestMethod.GET, params = "action=update")
    public String getExistedMealForUpdate(@RequestParam("id") int id, Model model) throws NotFoundException
    {
        LOG.info("Run: {} {}", getClass().getSimpleName(), "editMeal()");

        int userId = AuthorizedUser.id();
        Meal meal = mealService.get(id, userId);
        model.addAttribute("meal", meal);
        return "meal";
    }

    @RequestMapping(method = RequestMethod.GET, params = "action=delete")
    public String deleteMeal(@RequestParam("id") int id, Model model) throws NotFoundException
    {
        LOG.info("Run: {} {}", getClass().getSimpleName(), "deleteMeal()");
        int userId = AuthorizedUser.id();
        mealService.delete(id, userId);
        model.addAttribute("meals",
                MealsUtil.getWithExceeded(mealService.getAll(userId), AuthorizedUser.getCaloriesPerDay()));
        return "meals";
    }

    @RequestMapping(method = RequestMethod.POST, params = "!action")
    public String updateMeal(@ModelAttribute("meal") Meal meal, Model model) throws Exception
    {
        LOG.info("Run: {} {}", getClass().getSimpleName(), "updateMeal()");

        int userId = AuthorizedUser.id();

        if (meal.getId() != null)
        {
            checkIdConsistent(meal, meal.getId());
            mealService.update(meal, userId);
        } else
        {
            checkNew(meal);
            mealService.save(meal, userId);
        }
        model.addAttribute("meals", MealsUtil.getWithExceeded(mealService.getAll(userId), AuthorizedUser.getCaloriesPerDay()));
        return "meals";
    }

    @RequestMapping(method = RequestMethod.POST, params = "action=filter")
    public String filterMeals(
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "startTime", required = false) LocalTime startTime,
            @RequestParam(value = "endTime", required = false) LocalTime endTime,
            Model model)
    {
        LOG.info("Run: {} {}", getClass().getSimpleName(), "filterMeals()");

        if (ValidationUtil.filterFormIsEmpty(startDate, endDate, startTime, endTime))
            return "redirect:/meals";

        List<MealWithExceed> meals = MealsUtil.getFilteredWithExceeded(
                mealService.getBetweenDates(
                        startDate != null ? startDate : DateTimeUtil.MIN_DATE,
                        endDate != null ? endDate : DateTimeUtil.MAX_DATE,
                        AuthorizedUser.id()
                ),
                startTime != null ? startTime : LocalTime.MIN,
                endTime != null ? endTime : LocalTime.MAX,
                AuthorizedUser.getCaloriesPerDay()
        );
        model.addAttribute("meals", meals);
        return "meals";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String httpServletRequestListener(HttpServletRequest request)
    {
        LOG.info("Run: {} {}", getClass().getSimpleName(), "httpServletRequestListener()");
        return "404";
    }
}
