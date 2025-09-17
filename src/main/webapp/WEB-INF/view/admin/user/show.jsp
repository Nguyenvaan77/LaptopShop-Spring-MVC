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
                    Table users with user id = ${id}
                </h3>
            </div>
 
            <hr/>

            <div class="card" 60% modelAttribute="user">
                <div class="card-header">
                    User information
                </div>
                <ul class="list-group list-group-flush">
                    <li class="list-group-item">Id: ${user.id}</li>
                    <li class="list-group-item">Email: ${user.email}</li>
                    <li class="list-group-item">Fullname: ${user.fullName}</li>
                    <li class="list-group-item">Address: ${user.address}</li>
                    <li class="list-group-item">Phone: ${user.phoneNumber}</li>
                </ul>
            </div>
            <a href="/admin/user" class="btn btn-success">Back</a>
        </div>
    </div>
</body>
</html>