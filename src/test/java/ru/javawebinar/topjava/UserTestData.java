package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.User;

import java.util.Objects;

import static ru.javawebinar.topjava.model.BaseEntity.START_SEQ;

public class UserTestData {
    public static int USER_ID;
    public static int ADMIN_ID;

    public static User USER;
    public static User ADMIN;

    public static final ModelMatcher<User> MATCHER = new ModelMatcher<>(
            (expected, actual) -> expected == actual ||
                    (Objects.equals(expected.getPassword(), actual.getPassword())
                            && Objects.equals(expected.getId(), actual.getId())
                            && Objects.equals(expected.getName(), actual.getName())
                            && Objects.equals(expected.getEmail(), actual.getEmail())
                            && Objects.equals(expected.getCaloriesPerDay(), actual.getCaloriesPerDay())
                            && Objects.equals(expected.isEnabled(), actual.isEnabled())
//                            && Objects.equals(expected.getRoles(), actual.getRoles())
                    )
    );

    public static User getUser()
    {
        USER = new User(null, "User", "user@yandex.ru", "password", Role.ROLE_USER);
        return USER;
    }

    public static User getAdmin()
    {
        ADMIN = new User(null, "Admin", "admin@gmail.com", "admin", Role.ROLE_ADMIN);
        return ADMIN;
    }


}
