package ru.javawebinar.topjava.util.formatters;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import org.springframework.format.Formatter;

/**
 * Created by wwwtm on 17.05.2017.
 */
public class FormatLocalTimeAnnotationFormatterFactory
        implements AnnotationFormatterFactory<LocalTimeFormat>
{
    @Override
    public Set<Class<?>> getFieldTypes()
    {
        return new HashSet<>(Collections.singletonList(LocalTime.class));
    }

    @Override
    public Printer<?> getPrinter(LocalTimeFormat annotation, Class<?> fieldType)
    {
        return new LocalTimePatternFormatter(annotation.value());
    }

    @Override
    public Parser<?> getParser(LocalTimeFormat annotation, Class<?> fieldType)
    {
        return new LocalTimePatternFormatter(annotation.value());
    }

    private static class LocalTimePatternFormatter
            implements Formatter<LocalTime>
    {
        private Set<String> patterns = new HashSet<>(Arrays.asList(
                "HH:mm:ss.nnnnnnnnn",
                "HH mm ss",
                "HH:mm",
                "HH mm"
        ));

        public LocalTimePatternFormatter(String[] value)
        {
            patterns.addAll(Arrays.asList(value));
        }

        @Override
        public LocalTime parse(String text, Locale locale) throws ParseException
        {
            for (String pattern : patterns)
            {
                try
                {
                    return LocalTime.parse(text, DateTimeFormatter.ofPattern(pattern, locale));

                } catch (IllegalArgumentException | DateTimeParseException ignored)
                {
                    //toDO log pattern error
                }
            }

            throw new ParseException(String.format("%s: can't parse argument to LocalDate type.", getClass().getName()), 0);
        }

        @Override
        public String print(LocalTime object, Locale locale)
        {
            return object.format(DateTimeFormatter.ISO_DATE.withLocale(locale));
        }
    }
}
