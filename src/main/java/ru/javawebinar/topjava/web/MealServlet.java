package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * User: gkislin
 * Date: 19.08.2014
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);

    private ClassPathXmlApplicationContext applicationContext;
    private MealRepository repository;
    private UserRepository userRepository;
    //private AuthorizedUser authorizedUser;
    private String BAD_REQUEST = "404";
    private String NOT_FOUND = "400";

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        applicationContext = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        repository = applicationContext.getBean(MealRepository.class);
        userRepository = applicationContext.getBean(UserRepository.class);
        //authorizedUser = applicationContext.getBean(AuthorizedUser.class);
    }

    @Override
    public void destroy()
    {
        super.destroy();
        applicationContext.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MealRestController controller = getController();
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        if (id != null){
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.valueOf(request.getParameter("calories")));

            LOG.info(meal.isNew() ? "Create {}" : "Update {}", meal);

            try
            {
                controller.update(meal);
                response.sendRedirect("meals");

            } catch (NotFoundException e)
            {
                response.sendRedirect(NOT_FOUND);
            }

        } else {
            String fDate = request.getParameter("fromDate");
            String tDate = request.getParameter("toDate");
            String fTime = request.getParameter("fromTime");
            String tTime = request.getParameter("toTime");

            try
            {
                String datePattern = "yyyy-MM-dd";
                String timePattern = "HH:mm";

                LocalDate fromDate = !fDate.isEmpty() ? LocalDate.parse(fDate, DateTimeFormatter.ofPattern(datePattern)) : null;
                LocalDate toDate = !tDate.isEmpty() ? LocalDate.parse(tDate, DateTimeFormatter.ofPattern(datePattern)) : null;
                LocalTime fromTime = !fTime.isEmpty() ? LocalTime.parse(fTime, DateTimeFormatter.ofPattern(timePattern)) : null;
                LocalTime toTime = !tTime.isEmpty() ? LocalTime.parse(tTime, DateTimeFormatter.ofPattern(timePattern)) : null;

                request.setAttribute("fromDate", fDate);
                request.setAttribute("toDate", tDate);
                request.setAttribute("fromTime", fTime);
                request.setAttribute("toTime", tTime);

                request.setAttribute("meals", controller.getFilteredMeals(fromDate, toDate, fromTime, toTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);

            } catch (DateTimeParseException e)
            {
                // Bad request
                response.sendRedirect(BAD_REQUEST);

            } catch (NotFoundException e)
            {
                // Not found
                response.sendRedirect(NOT_FOUND);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MealRestController controller = getController();

        String action = request.getParameter("action");

        try
        {
            switch (action == null ? "all" : action) {
                case "delete":
                    int id = getId(request);
                    LOG.info("Delete {}", id);
                    controller.delete(id);
                    response.sendRedirect("meals");
                    break;
                case "create":
                case "update":
                    final Meal meal = action.equals("create") ?
                            controller.getBlank() :
                            controller.get(getId(request));
                    request.setAttribute("meal", meal);
                    request.getRequestDispatcher("/meal.jsp").forward(request, response);
                    break;
                case "all":
                default:
                    LOG.info("getAll");
                    request.setAttribute("meals", controller.getMeals());
                    request.getRequestDispatcher("/meals.jsp").forward(request, response);
                    break;
            }
        } catch (NotFoundException | NullPointerException e)
        {
            response.sendRedirect(BAD_REQUEST);
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }

    private MealRestController getController() throws ServletException
    {
        try
        {
            MealService service = applicationContext.getBean(MealService.class);
            MealRestController controller = applicationContext.getBean(MealRestController.class);
            Field field = service.getClass().getDeclaredField("repository");
            field.setAccessible(true);
            field.set(service, repository);
            field.setAccessible(false);

            field = service.getClass().getDeclaredField("users");
            field.setAccessible(true);
            field.set(service, userRepository);
            field.setAccessible(false);

            field = controller.getClass().getDeclaredField("service");
            field.setAccessible(true);
            field.set(controller, service);
            field.setAccessible(false);

            return controller;

        } catch (Exception e)
        {
            LOG.debug("Can't instantiate controller. Error message: " + e.getMessage());
            throw new ServletException(e);
        }
    }

    public class ServletDispatcher
    {
        private HttpServletRequest request;
        private HttpServletResponse response;

        private ServletDispatcher(HttpServletRequest request, HttpServletResponse response)
        {
            this.request = request;
            this.response = response;
        }

        public void forwardTo(String location) throws ServletException, IOException
        {
            request.getRequestDispatcher(location).forward(request, response);
        }

        public void setContent(String name, Object content)
        {
            request.setAttribute(name, content);
        }


        public void redirectTo(String location) throws IOException
        {
            response.sendRedirect(location);
        }

        public String getRequestParameter(String name)
        {
            return request.getParameter(name);
        }
    }

}