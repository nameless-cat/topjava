package ru.javawebinar.topjava.util.converters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Created by wwwtm on 06.05.2017.
 */
public class StringToLocalTimeConverter
        implements Converter<String, LocalTime>
{
    @Override
    public LocalTime convert(String source)
    {
        try
        {
            return LocalTime.parse(source);

        } catch (DateTimeParseException e)
        {
            return null;
        }
    }
}
