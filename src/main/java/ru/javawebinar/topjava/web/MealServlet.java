package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.FakeDB;
import ru.javawebinar.topjava.exception.DBEntityNotFoundException;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.slf4j.LoggerFactory.getLogger;


public class MealServlet
        extends HttpServlet
{
    private static final Logger LOG = getLogger(UserServlet.class);
    private SecurityUtil security = SecurityUtil.newInstance();
    private FakeDB db = FakeDB.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        if (!security.isRequestValid(request))
            response.sendRedirect("/404");

        if (security.isEmptyQueryString(request))
        {
            ConcurrentLinkedQueue<Meal> mealList = db.getMealList();
            List<MealWithExceed> mealWithExceeds = MealsUtil.getFilteredWithExceeded(mealList);
            request.setAttribute("mealList", mealWithExceeds);
            request.getRequestDispatcher("meals.jsp").forward(request, response);

        } else {

            switch (security.getAction(request))
            {
                case "new":
                    request.getRequestDispatcher("mealForm.jsp").forward(request, response);
                    break;

                case "edit":
                    handleEditAction(request, response);
                    break;

                case "delete":
                    handleDeleteAction(request, response);
                    break;
            }
        }
    }

    private void handleDeleteAction(HttpServletRequest request, HttpServletResponse response)
            throws IllegalArgumentException, IOException
    {
        try
        {
            db.delete(security.getId(request));
            response.sendRedirect("/topjava");

        } catch (DBEntityNotFoundException e)
        {
            // выдать сообщение о ненайденной записи
        }
    }

    private void handleEditAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, IllegalArgumentException
    {
        try
        {
            Meal meal = db.getById(security.getId(request));

            request.setAttribute("meal", meal);
            request.getRequestDispatcher("mealForm.jsp").forward(request, response);

        } catch (DBEntityNotFoundException e)
        {
            // сообщение о ненайденной записи
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        if (!security.isRequestValid(request))
        {
            // если в POST пришло неверное значение скрытого поля
            // значит была попытка править исходник на стороне клиента
            // думаю такого рода запросы можно смело бросать в корзину
            if (security.getErrorFields(request).contains("id"))
            {
                response.sendRedirect("/404");
                return;
            }

            // в случае невалидности одного из доступных пользователю полей
            // следует вернуть ему форму с данными в исходном состоянии
            try
            {
                request.setAttribute("meal", db.getById(security.getId(request)));
                setFormErrors(security.getErrorFields(request), request);
                request.getRequestDispatcher("mealForm.jsp").forward(request, response);

            } catch (DBEntityNotFoundException e)
            {
                // если после всех проверок восстановить исходные данные не удалось,
                // значит они были удалены другим запросом
                // сообщить клиенту о ненайденной записи и перенаправить его на главную страницу
            }

            return;
        }

        db.store(security.getObject(request));
        response.sendRedirect("/topjava");
    }

    private void setFormErrors(List<String> errorFields, HttpServletRequest request)
    {
        if (errorFields.contains("id"))
            request.setAttribute("idError", "Такая запись не существует");

        if (errorFields.contains("description"))
            request.setAttribute("descriptionError", "Обязательное поле");

        if (errorFields.contains("time"))
            request.setAttribute("timeError", "Нужно указать правильное время");

        if (errorFields.contains("calories"))
            request.setAttribute("caloriesError", "Трубется число, большее нуля");
    }

    @Override
    public void init() throws ServletException
    {
        LOG.debug("init servlet");
        super.init();
    }

    @Override
    public void destroy()
    {
        LOG.debug("destroy servlet");
        super.destroy();
    }
}
