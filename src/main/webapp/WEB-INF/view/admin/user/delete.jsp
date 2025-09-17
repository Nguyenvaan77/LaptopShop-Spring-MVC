<%@page contentType="text/html" pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>

    <!-- Latest compiled and minified CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- Latest compiled JavaScript -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<!-- jQuery (nếu bạn cần dùng thêm) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
</head>
<body>
    <div class="container">
    <h3>Delete User with id = ${userId}</h3>

    <hr/>

    <div >
        <form:form action="/admin/user/delete" modelAttribute="user" method="post">
            <div class="alert alert-warning" role="alert">
                Are you sure to delete user with id = ${id}?
            </div>

            <div style="display: none">
                <form:input type="number" path="id" class="form-control"/>
            </div>
            

            <button class="btn btn-danger" type="submit">
                Confirm
            </button>
        </form:form>
    </div>
</div>
</body>
</html>

