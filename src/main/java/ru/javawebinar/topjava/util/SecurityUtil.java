package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by wwwtm on 28.03.2017.
 */
public class SecurityUtil
{
    private static final Logger LOG = getLogger(SecurityUtil.class);

    private List<String> errorFields;
    private Meal resultObject;
    private List<String> queryGETActions = new ArrayList<>(Arrays.asList(
            "new",
            "delete",
            "edit"
    ));
    private final String SESSION_PARAMETER_OBTAIN_ERROR = "Session: cant't obtain %s parameter";

    private SecurityUtil()
    {
    }

    public boolean isRequestValid(HttpServletRequest request)
    {
        String requestMethod = request.getMethod();
        boolean validationResult;

        if (requestMethod.equals("GET"))
            validationResult = handleGET(request);
        else if (requestMethod.equals("POST"))
            validationResult = handlePOST(request);
        else
            validationResult = false;

        return validationResult;

    }

    private boolean handlePOST(HttpServletRequest request)
    {
        HttpSession session = request.getSession();

        long id = 0;
        LocalDateTime time = LocalDateTime.now();
        String description;
        int calories = 0;
        List<String> errorFields = new ArrayList<>();

        try
        {
            id = Long.parseLong(request.getParameter("id"));

            if (id < 1)
                throw new Exception();

            session.setAttribute("id", id);

        } catch (Exception e)
        {
            errorFields.add("id");
        }

        try
        {
            time = LocalDateTime.parse(request.getParameter("time"));
        } catch (Exception e)
        {
            errorFields.add("time");
        }

        try
        {
            calories = Integer.parseInt(request.getParameter("calories"));

            if (calories <= 0)
                throw new Exception();

        } catch (Exception e)
        {
            errorFields.add("calories");
        }

        description = request.getParameter("description");

        if (!description.matches("[a-zA-Zа-яА-ЯёЁ0-9_ ]+"))
            errorFields.add("description");

        session.setAttribute("errorFields", errorFields);

        if (errorFields.size() == 0)
            session.setAttribute("resultObject", new Meal(id, time, description, calories));


        return errorFields.size() == 0;
    }

    private boolean handleGET(HttpServletRequest request)
    {
        HttpSession session = request.getSession();

        if (request.getQueryString() == null)
            return true;

        String action = request.getParameter("action");
        session.setAttribute("action", action);

        if (action == null || action.isEmpty())
            return false;

        if (!queryGETActions.contains(action))
            return false;

        if (action.equals("edit") || action.equals("delete"))
        {
            String idParam = request.getParameter("id");

            if (idParam == null)
                return false;

            try
            {
                session.setAttribute("id", Long.parseLong(idParam));

            } catch (NumberFormatException e)
            {
                return false;
            }
        }

        return true;
    }

    public List<String> getErrorFields(HttpServletRequest request)
    {
        List<String> result = new ArrayList<>();

        try
        {
            result = (List<String>) request.getSession().getAttribute("errorFields");

        } catch (ClassCastException e)
        {

        }

        return result;
    }

    public static SecurityUtil newInstance()
    {
        return new SecurityUtil();
    }

    public Meal getObject(HttpServletRequest request)
    {
        Meal meal;

        try
        {
            meal = (Meal) request.getSession().getAttribute("resultObject");

        } catch (ClassCastException e)
        {
            LOG.debug(String.format(SESSION_PARAMETER_OBTAIN_ERROR, "meal"));
            // safe plug
            meal = new Meal(0L, LocalDateTime.now(), "", 0);
        }

        return meal;
    }

    public long getId(HttpServletRequest request)
    {
        long id;

        try
        {
            id = (long) request.getSession().getAttribute("id");

        } catch (ClassCastException e)
        {
            LOG.debug(String.format(SESSION_PARAMETER_OBTAIN_ERROR, "id"));
            id = 0;
        }

        return id;
    }

    public String getAction(HttpServletRequest request)
    {
        String action;

        try
        {
            action = (String) request.getSession().getAttribute("action");

        } catch (ClassCastException e)
        {
            LOG.debug(String.format(SESSION_PARAMETER_OBTAIN_ERROR, "action"));
            action = "new";
        }

        return action;
    }

    public boolean isEmptyQueryString(HttpServletRequest request)
    {

        return request.getQueryString() == null;
    }
}
