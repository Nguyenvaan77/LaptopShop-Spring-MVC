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
        <div class="col-md-6 col-12 mx-auto">
            <legend>Update user</legend>

            <form:form action="/admin/user/update" method="post" modelAttribute="newUser">
                <div style="display: none">
                    <div> 
                        <label for="idInput" class="form-label">Id</label>
                    </div>

                    <div>
                        <form:input placeholder="Your id" type="text" id="idInput" name="" class="form-control" 
                        path="id" />
                    </div>
                </div>

                <div>
                    <div> 
                        <label for="emailInput" class="form-label">Email</label>
                    </div>

                    <div>
                        <form:input placeholder="Confirm your email" type="text" id="emailInput" name="" class="form-control" 
                        path="email" disabled="true"/>
                    </div>
                </div>

                <div>
                    <div>
                        <label for="inputName" class="form-label">Fullname</label>
                    </div>

                    <div>
                        <form:input type="text" name="" id="inputName" class="form-control"
                        path="fullName"/>
                    </div>
                </div>

                <div>
                    <div>
                        <label for="inputAdress" class="form-label">Address</label>
                    </div>

                    <div>
                        <form:input type="text" name="" id="inputAdress" class="form-control"
                        path="address"/>
                    </div>
                </div>

                <div>
                    <div>
                        <label for="inputPhoneNumber" class="form-label">Phone number</label>
                    </div>
                    
                    <div>
                        <form:input type="number" name="" id="inputPhoneNumber" class="form-control"
                        path="phoneNumber"/>
                    </div>
                </div>

                <button type="submit" class="btn btn-warning">Update</button> 
            </form:form>
        </div>
        
    </div>
</body>
</html>