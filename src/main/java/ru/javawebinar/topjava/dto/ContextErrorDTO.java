package ru.javawebinar.topjava.dto;

/**
 * Created by wwwtm on 15.05.2017.
 */
public class ContextErrorDTO
    implements Comparable<ContextErrorDTO>
{
    private String errorContext;
    private String message;

    public ContextErrorDTO()
    {
    }

    public ContextErrorDTO(String errorContext, String message)
    {
        this.errorContext = errorContext;
        this.message = message;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContextErrorDTO errorDTO = (ContextErrorDTO) o;

        if (errorContext != null ? !errorContext.equals(errorDTO.errorContext) : errorDTO.errorContext != null) return false;
        return message != null ? message.equals(errorDTO.message) : errorDTO.message == null;
    }

    @Override
    public int hashCode()
    {
        int result = errorContext != null ? errorContext.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(ContextErrorDTO o)
    {
        return errorContext.compareTo(o.getErrorContext());
    }

    public String getErrorContext()
    {
        return errorContext;
    }
}
