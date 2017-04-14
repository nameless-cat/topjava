package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcMealRepositoryImpl
        implements MealRepository
{
    private static final RowMapper<Meal> ROW_MAPPER = new RowMapper<Meal>()
    {
        @Override
        public Meal mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            return new Meal(
                    rs.getInt("id"),
                    rs.getTimestamp("date_time").toLocalDateTime(),
                    rs.getString("description"),
                    rs.getInt("calories"));
        }
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertMeal;

    @Autowired
    public JdbcMealRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate)
    {
        this.insertMeal = new SimpleJdbcInsert(dataSource)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Meal save(Meal meal, int userId)
    {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("user_id", userId)
                .addValue("description", meal.getDescription())
                .addValue("date_time", meal.getDateTime())
                .addValue("calories", meal.getCalories());

        if (meal.isNew())
        {
            Number newKey = insertMeal.executeAndReturnKey(map);
            meal.setId(newKey.intValue());

        } else {
            int rows = namedParameterJdbcTemplate.update(
                    "UPDATE meals SET description=:description, date_time=:date_time, " +
                            "calories=:calories WHERE id=:id AND user_id=:user_id", map);
            // если запись не найдена, то нужно это показать
            if (rows == 0)
                meal = null;
        }

        return meal;
    }

    @Override
    public boolean delete(int id, int userId)
    {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId)
    {
        List<Meal> result = jdbcTemplate.query("SELECT * FROM meals WHERE id=? AND user_id=?", ROW_MAPPER, id, userId);
        return CollectionUtils.isEmpty(result) ? null : DataAccessUtils.singleResult(result);
    }

    @Override
    public List<Meal> getAll(int userId)
    {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id=?", ROW_MAPPER, userId)
                .stream()
                .sorted((m, u) -> u.getDateTime().compareTo(m.getDateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId)
    {
        return getAll(userId)
                .stream()
                .filter(m -> DateTimeUtil.isBetween(m.getDateTime(), startDate, endDate))
                .collect(Collectors.toList());
    }
}
