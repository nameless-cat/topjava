package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;

/**
 * Created by wwwtm on 10.05.2017.
 */
public class UserBuilder
{
    private User user = null;

    public UserBuilder()
    {
        user = new User();
    }

    public UserBuilder withId(int id)
    {
        user.setId(id);
        return this;
    }


    public UserBuilder withEmail(String email)
    {
        user.setEmail(email);
        return this;
    }

    public UserBuilder withName(String name)
    {
        user.setName(name);
        return this;
    }


    public UserBuilder withPassword(String password)
    {
        user.setPassword(password);
        return this;
    }

    public UserBuilder withRegistration(LocalDateTime localDateTime)
    {
        user.setRegistered(localDateTime);
        return this;
    }

    public UserBuilder withEnabledFlag(boolean enabled)
    {
        user.setEnabled(enabled);
        return this;
    }


    public UserBuilder withCalories(int calories)
    {
        user.setCaloriesPerDay(calories);
        return this;
    }

    public User build()
    {
        return user;
    }
}
