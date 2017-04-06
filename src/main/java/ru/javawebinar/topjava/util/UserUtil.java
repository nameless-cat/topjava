package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wwwtm on 02.04.2017.
 */
public class UserUtil
{
    public static final List<User> USERS = Arrays.asList(
            new User("User5", "user5@meal.ru", "1234", Role.ROLE_USER),
            new User("User4", "user4@meal.ru", "1234", Role.ROLE_USER),
            new User("User2", "user2@meal.ru", "1234", Role.ROLE_ADMIN),
            new User("User1", "user1@meal.ru", "1234", Role.ROLE_USER),
            new User("User3", "user3@meal.ru", "1234", Role.ROLE_ADMIN)
            );
}
