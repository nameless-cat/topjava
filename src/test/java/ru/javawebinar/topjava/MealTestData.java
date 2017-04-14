package ru.javawebinar.topjava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MealTestData
{
    public static final Comparator<Meal> COMPARATOR = (m, k) -> k.getDateTime().compareTo(m.getDateTime());

    public static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    public static final int ADMIN_ID = 100001;

    public static final int USER_ID = 100000;

    public static final List<Meal> USER_MEALS;

    public static final List<Meal> ADMIN_MEALS;


    static
    {

        LocalTime breakfastTime = LocalTime.of(9, 5);
        LocalTime lunchTime = LocalTime.of(14, 15);
        LocalTime dinnerTime = LocalTime.of(20, 30);
        LocalDate march = LocalDate.of(2017, 3,1);
        LocalDate april = LocalDate.of(2017, 4, 1);

        USER_MEALS = new ArrayList<>(Arrays.asList(
                new Meal(100002, LocalDateTime.of(april, breakfastTime), "Завтрак пользователя", 1200),
                new Meal(100003, LocalDateTime.of(april, lunchTime), "Обед пользователя", 600),
                new Meal(100004, LocalDateTime.of(april, dinnerTime), "Ужин пользователя", 300),
                new Meal(100005, LocalDateTime.of(march, breakfastTime), "Завтрак пользователя", 1200),
                new Meal(100006, LocalDateTime.of(march, lunchTime), "Обед пользователя", 600),
                new Meal(100007, LocalDateTime.of(march, dinnerTime), "Ужин пользователя", 300)

        )).stream()
        .sorted(COMPARATOR)
        .collect(Collectors.toList());

        ADMIN_MEALS = new ArrayList<>(Arrays.asList(
                new Meal(100008, LocalDateTime.of(march, breakfastTime), "Завтрак админа", 800),
                new Meal(100009, LocalDateTime.of(march, lunchTime), "Обед админа", 500),
                new Meal(100010, LocalDateTime.of(march, dinnerTime), "Ужин админа", 200)
        )).stream()
        .sorted(COMPARATOR)
        .collect(Collectors.toList());
    }

    public static final ModelMatcher<Meal> MATCHER = new ModelMatcher<>(
            (expected, actual) -> expected == actual ||
                    (Objects.equals(expected.getId(), actual.getId()))
                            && (Objects.equals(expected.getDescription(), actual.getDescription()))
                            && (Objects.equals(expected.getDateTime(), actual.getDateTime()))
                            && (Objects.equals(expected.getCalories(), actual.getCalories()))
    );

    private static Map<Integer, Map<Integer, Meal>> renewData()
    {
        Map<Integer, Map<Integer, Meal>> result = new HashMap<>();
        result.put(USER_ID, new HashMap<>());
        result.put(ADMIN_ID, new HashMap<>());

        LocalTime breakfastTime = LocalTime.of(9, 5);
        LocalTime lunchTime = LocalTime.of(14, 15);
        LocalTime dinnerTime = LocalTime.of(20, 30);
        LocalDate march = LocalDate.of(2017, 3,1);
        LocalDate april = LocalDate.of(2017, 4, 1);

        List<Meal> mealList = new ArrayList<>(Arrays.asList(
                new Meal(100002, LocalDateTime.of(april, breakfastTime), "Завтрак пользователя", 1200),
                new Meal(100003, LocalDateTime.of(april, lunchTime), "Обед пользователя", 600),
                new Meal(100004, LocalDateTime.of(april, dinnerTime), "Ужин пользователя", 300),
                new Meal(100005, LocalDateTime.of(march, breakfastTime), "Завтрак пользователя", 1200),
                new Meal(100006, LocalDateTime.of(march, lunchTime), "Обед пользователя", 600),
                new Meal(100007, LocalDateTime.of(march, dinnerTime), "Ужин пользователя", 300),
                new Meal(100008, LocalDateTime.of(march, breakfastTime), "Завтрак админа", 800),
                new Meal(100009, LocalDateTime.of(march, lunchTime), "Обед админа", 500),
                new Meal(100010, LocalDateTime.of(march, dinnerTime), "Ужин админа", 200)
        ));

        for (Meal meal : mealList.subList(0, 6))
            result.get(100000).put(meal.getId(), meal);

        for(Meal meal : mealList.subList(6, mealList.size()))
            result.get(100001).put(meal.getId(), meal);

        return result;
    }

    public static Map<Integer, Map<Integer, Meal>> getData()
    {
        return renewData();
    }

    public static void main(String[] args)
    {
        Map<Integer, Map<Integer, Meal>> integerMapMap = renewData();
        System.out.println("done");
    }
}
