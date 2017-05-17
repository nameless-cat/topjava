package ru.javawebinar.topjava.dto;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by wwwtm on 16.05.2017.
 */
public class ValidationErrorsDTO
{
    private Set<ContextErrorDTO> errors = new TreeSet<>();

    public ValidationErrorsDTO()
    {
    }

    public ValidationErrorsDTO(String errorContext, String message)
    {
        errors.add(new ContextErrorDTO(errorContext, message));
    }

    public ValidationErrorsDTO addField(String field, String message)
    {
        errors.add(new ContextErrorDTO(field, message));

        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValidationErrorsDTO that = (ValidationErrorsDTO) o;

        return errors.equals(that.errors);
    }

    @Override
    public int hashCode()
    {
        return errors.hashCode();
    }
}
