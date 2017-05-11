package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by wwwtm on 10.05.2017.
 */
public class UserRoleBatchPreparedStatementSetter implements BatchPreparedStatementSetter
{
    private User user;

    public UserRoleBatchPreparedStatementSetter(User user)
    {
        this.user = user;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException
    {
        Role[] roles = user.getRoles().toArray(new Role[]{});
        ps.setInt(1, user.getId());
        ps.setString(2, String.valueOf(roles[i]));
    }

    @Override
    public int getBatchSize()
    {
        return user.getRoles().size();
    }
}
