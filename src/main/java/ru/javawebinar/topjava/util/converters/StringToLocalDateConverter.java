package ru.javawebinar.topjava.util.converters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Created by wwwtm on 06.05.2017.
 */
public class StringToLocalDateConverter implements Converter<String, LocalDate>
{
    @Override
    public LocalDate convert(String source)
    {
        return LocalDate.parse(source);
    }
}