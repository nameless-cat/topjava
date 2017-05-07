package ru.javawebinar.topjava.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import ru.javawebinar.topjava.model.BaseEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.time.LocalDate;
import java.time.LocalTime;

public class ValidationUtil {
    private ValidationUtil() {
    }

    private static final Logger LOG = LoggerFactory.getLogger(ValidationUtil.class);

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        return checkNotFound(object, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(BaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void checkIdConsistent(BaseEntity entity, int id) {
//      http://stackoverflow.com/a/32728226/548473
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.getId() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }

    public static boolean filterFormIsEmpty(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime)
    {
        return (startDate == null && endDate == null && startTime == null && endTime == null);
    }

    public static boolean hasInvalidCriticalFields(BindingResult bindingResult)
    {
        for (String field : new String[]{"description", "dateTime", "calories"})
        {
            if (bindingResult.getFieldError(field) != null)
                return true;
        }

        return false;
    }
}