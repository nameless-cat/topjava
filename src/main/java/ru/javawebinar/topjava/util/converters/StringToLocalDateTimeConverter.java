package ru.javawebinar.topjava.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Created by wwwtm on 17.05.2017.
 */
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime>
{
    @Override
    public LocalDateTime convert(String source)
    {
        return LocalDateTime.parse(source);
    }

}
