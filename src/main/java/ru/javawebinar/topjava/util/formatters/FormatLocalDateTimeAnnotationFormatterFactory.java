package ru.javawebinar.topjava.util.formatters;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by wwwtm on 17.05.2017.
 */
public class FormatLocalDateTimeAnnotationFormatterFactory
        implements AnnotationFormatterFactory<LocalDateTimeFormat>
{
    @Override
    public Set<Class<?>> getFieldTypes()
    {
        return new HashSet<>(Arrays.asList(LocalDateTime.class));
    }

    @Override
    public Printer<?> getPrinter(LocalDateTimeFormat annotation, Class<?> fieldType)
    {
        return new LocalDateTimePatternFormatter(annotation.value());
    }

    @Override
    public Parser<?> getParser(LocalDateTimeFormat annotation, Class<?> fieldType)
    {
        return new LocalDateTimePatternFormatter(annotation.value());
    }

    private static class LocalDateTimePatternFormatter
            implements Formatter<LocalDateTime>
    {
        private String[] patterns;

        public LocalDateTimePatternFormatter(String[] patterns)
        {
            this.patterns = patterns;
        }

        @Override
        public LocalDateTime parse(String text, Locale locale) throws ParseException
        {
            for (String pattern : patterns)
            {
                try
                {
                    return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(pattern, locale));

                } catch (IllegalArgumentException | DateTimeParseException ignored)
                {
                    //toDO log pattern error
                }
            }

            throw new ParseException(String.format("%s: can't parse argument to LocalDateTime type.", getClass().getName()), 0);
        }

        @Override
        public String print(LocalDateTime object, Locale locale)
        {
            return object.format(DateTimeFormatter.ISO_DATE_TIME.withLocale(locale));
        }
    }

}
