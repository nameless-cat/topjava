package ru.javawebinar.topjava.util.formatters;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Created by wwwtm on 17.05.2017.
 */
public class FormatLocalDateAnnotationFormatterFactory
        implements AnnotationFormatterFactory<LocalDateFormat>
{
    @Override
    public Set<Class<?>> getFieldTypes()
    {
        return new HashSet<>(Collections.singletonList(LocalDate.class));
    }

    @Override
    public Printer<?> getPrinter(LocalDateFormat annotation, Class<?> fieldType)
    {
        return new LocalDatePatternFormatter(annotation.value());
    }

    @Override
    public Parser<?> getParser(LocalDateFormat annotation, Class<?> fieldType)
    {
        return new LocalDatePatternFormatter(annotation.value());
    }

    private static class LocalDatePatternFormatter
            implements Formatter<LocalDate>
    {
        private Set<String> patterns = new HashSet<>(Arrays.asList(
                "yyyy-MM-dd",
                "dd-MM-yyyy",
                "yyyy/MM/dd",
                "dd/MM/yyyy",
                "dd.MM.yyyy",
                "yyyy.MM.dd",
                "dd LLL yyyy",
                "dd MM yyyy",
                "yyyy MM dd",
                "dd MMM yyyy",
                "yyyy MMM dd"

        ));

        public LocalDatePatternFormatter(String[] value)
        {
            patterns.addAll(Arrays.asList(value));
        }

        @Override
        public LocalDate parse(String text, Locale locale) throws ParseException
        {
            for (String pattern : patterns)
            {
                try
                {
                    return LocalDate.parse(text, DateTimeFormatter.ofPattern(pattern, locale));

                } catch (IllegalArgumentException | DateTimeParseException ignored)
                {
                    //toDO log pattern error
                }
            }

            throw new ParseException(String.format("%s: can't parse argument to LocalDate type.", getClass().getName()), 0);
        }

        @Override
        public String print(LocalDate object, Locale locale)
        {
            return object.format(DateTimeFormatter.ISO_DATE.withLocale(locale));
        }
    }
}
