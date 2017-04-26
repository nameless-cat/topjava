package ru.javawebinar.topjava.repository.jdbc;

import java.time.LocalDateTime;

/**
 * Created by wwwtm on 26.04.2017.
 */
public interface JdbcMealRepositoryHelper
{
    Object getDateTime(LocalDateTime dateTime);
}
