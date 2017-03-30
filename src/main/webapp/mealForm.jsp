<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meal form edit</title>
</head>
<body>
<c:set var="queryString" value="?action=${meal == null ? 'new' : 'edit&id='.concat(meal.getId())}" />

<form method="post" enctype="application/x-www-form-urlencoded" action="/topjava${queryString}">

    <c:if test="${idError != null}">
        <div class="error">
            <span>${idError}</span>
        </div>
    </c:if>

    <table>
        <tr>
            <th colspan="2">Edit meal</th>
        </tr>
        <tr>
            <input type="hidden" name="id" value="${meal != null ? meal.getId() : ""}"/>
            <td><label for="description">Description</label></td>
            <div class="error">
                <span>${descriptionError != null ? descriptionError : ""}</span>
            </div>
            <td><input name="description" id="description" size="25" maxlength="25" placeholder="alphabetic, digits, -"
                       value="${meal != null ? meal.getDescription() : ""}"/></td>
        </tr>
        <tr>
            <td><label for="time">Time</label></td>
            <div class="error">
                <span>${timeError != null ? timeError : ""}</span>
            </div>
            <td><input type="datetime" name="time" id="time" size="20" maxlength="20" value="${meal != null ? meal.getDateTime() : ""}"/></td>
        </tr>
        <tr>
            <td><label for="calories">Calories</label></td>
            <div class="error">
                <span>${caloriesError != null ? caloriesError : ""}</span>
            </div>
            <td><input type="text" name="calories" id="calories" value="${meal != null ? meal.getCalories() : ""}" /></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" class="blue-button"/></td>
        </tr>
    </table>
</form>
</body>
</html>
