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
        <div class="col-12 mx-auto">
            <div class="d-flex justify-content-between">
                <h3>
                    Table users
                </h3>
                <div class="btn btn-primary">
                    <a href="/admin/user/create">Create a user</a>
                </div>
            </div>

            <hr/>
            <table class="table table-bordered">
                <thead class="table-active">
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Email</th>
                        <th scope="col">FullName</th>
                        <th scope="col">Action</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var ="user" items = "${users}">
                        <tr>
                            <th scope="row">${user.id}</th>
                            <td>${user.email}</td>
                            <td>${user.fullName}</td>
                            <td>
                                <div class="button">
                                <a href="/admin/user/${user.id}" class = "btn btn-primary">View</a>
                                <button type="button" class = "btn btn-warning">Update</button>
                                <button type="button" class = "btn btn-danger">Delete</button>
                            </div>
                            </td>
                        </tr>
                    </c:forEach>
                    
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>