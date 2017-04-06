<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h2><a href="index.html">Home</a></h2>
    <h2>Meal list</h2>
    <a href="meals?action=create">Add Meal</a>
    <hr>
    <form enctype="application/x-www-form-urlencoded" action="/meals" method="post">
        <table class="filter_box" cellpadding="8" cellspacing="5">
            <thead>
            <tr>
                <th>By date</th>
                <th>By time</th>
            </tr>
            </thead>
            <tr>
                <td>
                    <%--<label for="from_date">from</label>--%>
                    <input type="date" name="fromDate" id="from_date" value="${fromDate}">
                </td>
                <td>
                    <%--<label for="from_time">from</label>--%>
                    <input type="time" name="fromTime" id="from_time" value="${fromTime}">
                </td>
            </tr>
            <tr>
                <td>
                    <%--<label for="to_date">to</label>--%>
                    <input type="date" name="toDate" id="to_date" value="${toDate}">
                </td>
                <td>
                    <%--<label for="to_time">to</label>--%>
                    <input type="time" name="toTime" id="to_time" value="${toTime}">
                </td>
            </tr>
            <tr>
                <td>
                    <input type="submit" value="Filter" />
                </td>
            </tr>
        </table>
    </form>
    <hr>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>