package ru.javawebinar.topjava.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import ru.javawebinar.topjava.dto.FilterObject;
import ru.javawebinar.topjava.model.BaseEntity;
import ru.javawebinar.topjava.util.exception.NotFoundException;

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

    public static boolean filterFormIsEmpty(FilterObject filterObject)
    {
        return (filterObject.getStartDate() == null
                && filterObject.getEndDate() == null
                && filterObject.getStartTime() == null
                && filterObject.getEndTime() == null);
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