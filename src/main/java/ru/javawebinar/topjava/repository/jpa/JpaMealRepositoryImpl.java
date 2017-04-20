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
        else if (em.find(Meal.class, meal.getId()).getUser().getId() != userId)
        {
            return null;
        }
        else
        {
            return em.merge(meal);
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId)
    {
        Meal m = em.find(Meal.class, id);

        if (m.getUser().getId() == userId)
        {
            em.remove(m);
            return em.find(Meal.class, id) == null;
        }
        else
            return false;
    }

    @Override
    public Meal get(int id, int userId)
    {
        Meal m = em.find(Meal.class, id);

        return (m.getUser().getId() == userId) ? m : null;
    }

    @Override
    public List<Meal> getAll(int userId)
    {
        Query q = em.createNamedQuery(Meal.GET_ALL).setParameter("userId", userId);
        return (List<Meal>) q.getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId)
    {
        Query q = em.createNamedQuery(Meal.GET_BETWEEN)
                .setParameter("userId", userId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate);

        return (List<Meal>) q.getResultList();
    }
}