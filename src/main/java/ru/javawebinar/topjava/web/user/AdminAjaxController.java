package ru.javawebinar.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(AdminAjaxController.BASE_URL)
public class AdminAjaxController extends AbstractUserController {

    public static final String BASE_URL = "/ajax/admin/users";

    @Autowired
    public AdminAjaxController(UserService service) {
        super(service);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @PostMapping
    public void createOrUpdate(@RequestParam("id") Integer id,
                               @RequestParam("name") String name,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password) {

        User user = new User(id, name, email, password, Role.ROLE_USER);
        if (user.isNew()) {
            super.create(user);
        } else {
            super.update(user, id);
        }
    }

    @PutMapping(value = "/{id}")
    public void switchActiveStatus(@PathVariable int id, @RequestParam boolean enabled)
            throws NullPointerException, AccessDeniedException, NotFoundException
    {
        User current = super.get(AuthorizedUser.id());

        if (!current.getRoles().contains(Role.ROLE_ADMIN))
        {
            throw new AccessDeniedException("Forbidden operation");
        }

        super.switchActiveStatus(id, enabled);
    }
}
