package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

import static javafx.scene.input.KeyCode.T;
@Transactional(readOnly = true)
public interface CrudMealRepository
        extends JpaRepository<Meal, Integer>
{
    @Transactional
    @Modifying
    @Query("UPDATE Meal m SET m.dateTime=:#{#meal.dateTime}, " +
            "m.calories= :#{#meal.calories}, " +
            "m.description=:#{#meal.description} " +
            "WHERE m.id=:#{#meal.id} AND m.user.id=:#{#userId}")
    int update(@Param("meal") Meal meal, @Param("userId") int userId);

    @Transactional
    Long deleteByIdAndUserId(int id, int userId);

    @Transactional
    Meal save(Meal meal);

    Meal findByIdAndUserId(int id, int userId);

    List<Meal> findByUserIdOrderByDateTimeDesc(int userId);

    List<Meal> findByUserIdAndDateTimeBetweenOrderByDateTimeDesc(int userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT m FROM Meal m JOIN FETCH m.user WHERE m.id=?1")
    Meal getWithUser(int id);
}
