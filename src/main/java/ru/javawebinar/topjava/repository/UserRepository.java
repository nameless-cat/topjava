package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.User;

import javax.naming.OperationNotSupportedException;
import java.util.List;

public interface UserRepository {
    User save(User user);

    // false if not found
    boolean delete(int id);

    // null if not found
    User get(int id);

    // null if not found
    User getByEmail(String email);

    List<User> getAll();

    default User getWithMeals(int id){
        throw new UnsupportedOperationException();
    }

    default boolean switchActiveStatus(int userId, boolean enabled)
    {
        throw new RuntimeException("Operation not supported");
    }
}