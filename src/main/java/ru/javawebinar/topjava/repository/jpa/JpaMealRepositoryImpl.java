package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl
        implements MealRepository
{
    @PersistenceContext
    EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId)
    {
        User u = em.getReference(User.class, userId);
        meal.setUser(u);

        if (meal.isNew())
        {
            em.persist(meal);

            return meal;
        }
        else if (get(meal.getId(), userId) == null)
        {
            return null;
        }
        else
        {
            em.merge(meal);
        }

        return meal;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId)
    {
        return em.createNamedQuery(User.DELETE)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId)
    {
        Meal m = em.find(Meal.class, id);

        return (m != null && m.getUser().getId() == userId) ? m : null;
    }

    @Override
    public List<Meal> getAll(int userId)
    {
        return em.createNamedQuery(Meal.GET_ALL, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId)
    {
        return em.createNamedQuery(Meal.GET_BETWEEN, Meal.class)
                .setParameter("userId", userId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
}