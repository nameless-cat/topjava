package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.MealBuilder;
import ru.javawebinar.topjava.util.UserBuilder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class JdbcUserRepositoryImpl
        implements UserRepository
{

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;


    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate)
    {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public User save(User user)
    {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew())
        {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", new UserRoleBatchPreparedStatementSetter(user));
        } else
        {
            namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource);
        }

        return user;
    }

    @Override
    public boolean delete(int id)
    {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id)
    {
        UserResultSetRowMapper rowMapper = new UserResultSetRowMapper();
        jdbcTemplate.query("SELECT * FROM users INNER JOIN user_roles " +
                "ON users.id=user_roles.user_id WHERE id=?", rowMapper, id);
        return DataAccessUtils.singleResult(rowMapper.getResultList());
    }

    @Override
    public User getByEmail(String email)
    {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        UserResultSetRowMapper rowMapper = new UserResultSetRowMapper();
        jdbcTemplate.query("SELECT * FROM users INNER JOIN user_roles " +
                "ON users.id=user_roles.user_id WHERE email=?", rowMapper, email);
        return DataAccessUtils.singleResult(rowMapper.getResultList());
    }

    @Override
    public List<User> getAll()
    {
        UserResultSetRowMapper rowMapper = new UserResultSetRowMapper();
        jdbcTemplate.query("SELECT * FROM users INNER JOIN user_roles " +
                "ON users.id=user_roles.user_id ORDER BY name, email", rowMapper);
        return rowMapper.getResultList();
    }
}
