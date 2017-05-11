package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.jdbc.core.RowCallbackHandler;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.MealBuilder;
import ru.javawebinar.topjava.util.UserBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wwwtm on 10.05.2017.
 */
public class UserResultSetRowMapper implements RowCallbackHandler
{
    private List<User> resultList = new ArrayList<>();

    private User currentMappingObject = null;

    /**
     * ResultSet must be ordered by user id field
     * in sake of consistency of result data list
     */
    @Override
    public void processRow(ResultSet rs) throws SQLException
    {
        int userId = rs.getInt("id");

        if (currentMappingObject == null || currentMappingObject.getId() != userId)
        {
            currentMappingObject = new UserBuilder()
                    .withId(userId)
                    .withEmail(rs.getString("email"))
                    .withName(rs.getString("name"))
                    .withPassword(rs.getString("password"))
                    .withRegistration(rs.getTimestamp("registered").toLocalDateTime())
                    .withEnabledFlag(rs.getBoolean("enabled"))
                    .withCalories(rs.getInt("calories_per_day"))
                    .build();

            resultList.add(currentMappingObject);
        }

        String userRole = rs.getString("role");
        if (userRole != null)
        {
            if (currentMappingObject.getRoles() == null)
            {
                currentMappingObject.setRoles(new LinkedHashSet<Role>());
            }

            currentMappingObject.getRoles().add(Role.valueOf(userRole));
        }


        /**
         * Нужен дополнительный экстрактор поля
         * чтобы разрешать проблему одинаковых названий полей
         * в разных таблицах
         */

        /*Integer mealId = rs.getInt("meals.id");
        if (mealId != null)
        {
            currentMappingObject.getMeals().add(
                    new MealBuilder()
                            .withId(mealId)
                            .withUser(currentMappingObject)
                            .withDateTime(rs.getTimestamp("meals.date_time").toLocalDateTime())
                            .withDescription(rs.getString("meals.description"))
                            .withCalories(rs.getInt("meals.calories"))
                            .build()
            );
        }*/

    }

    public List<User> getResultList()
    {
        return resultList;
    }
}
