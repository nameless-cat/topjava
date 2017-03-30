<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html>
<head>
    <title>Meal list with exceeded</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="/resources/style.css" type="text/css">
</head>
<body>
<c:if test="${!empty mealList}">
    <table class="tg">
        <tr>
            <th>Description</th>
            <th>Time</th>
            <th>Calories</th>
            <th>Edit</th>
            <th>Delete</th>
        </tr>
        <c:forEach items="${mealList}" var="meal">
            <tr>
                <td>${meal.getDescription()}</td>
                <td><javatime:format value="${meal.getDateTime()}" style="FF" pattern="dd-MM-yyyy HH:mm"/></td>
                <td class="${meal.isExceed() ? "exceed" : ""}">
                        ${meal.getCalories()}
                </td>
                <td><a href="/topjava?action=edit&id=${meal.getId()}">Edit</a></td>
                <td><a href="/topjava?action=delete&id=${meal.getId()}">Delete</a></td>

            </tr>
        </c:forEach>
    </table>
</c:if>
</body>
</html>
