package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.NamedEntity;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.UserUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl
        implements UserRepository
{
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);

    private Map<Integer, User> repository = new ConcurrentHashMap<>();

    private AtomicInteger count = new AtomicInteger(1);

    public InMemoryUserRepositoryImpl()
    {
        UserUtil.USERS.forEach(this::save);
    }

    @Override
    public boolean delete(int id)
    {
        LOG.info("delete " + id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user)
    {
        LOG.info("save " + user);

        if (user.isNew())
            user.setId(count.getAndIncrement());

        repository.put(user.getId(), user);

        return user;
    }

    @Override
    public User get(int id)
    {
        LOG.info("get " + id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll()
    {
        LOG.info("getAll");
        return repository.values()
                .stream()
                .sorted(Comparator.comparing(NamedEntity::getName))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email)
    {
        LOG.info("getByEmail " + email);

        try
        {
            User user = getAll()
                    .stream()
                    .filter(u -> u.getEmail().equals(email))
                    .findAny()
                    .get();

            return user;

        } catch (NoSuchElementException e)
        {
            return null;
        }
    }

    public static void main(String[] args)
    {
    }
}
