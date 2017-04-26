package ru.javawebinar.topjava.repository.jdbc;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by wwwtm on 26.04.2017.
 */
public class JdbcMealRepositoryHelperHsqlDb
        implements JdbcMealRepositoryHelper
{
    @Override
    public Object getDateTime(LocalDateTime dateTime)
    {
        return Timestamp.valueOf(dateTime);
    }
}
