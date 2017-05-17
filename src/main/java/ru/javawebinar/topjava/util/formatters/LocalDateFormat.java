package ru.javawebinar.topjava.util.formatters;

import java.lang.annotation.*;
import java.time.format.DateTimeFormatter;

/**
 * Created by wwwtm on 17.05.2017.
 */
@Target(value={ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LocalDateFormat
{
    /**
     * Specifies a template according to which the string should be converted to LocalDate type
     * In case of list the first suitable pattern is picked
     * @see DateTimeFormatter docs <b>Patterns for Formatting and Parsing<b/> section for legal pattern symbols
     * @return an array of preferable patterns to parse string argument
     */
    String[] value();
}
