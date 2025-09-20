<%@page contentType="text/html" pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Dashboard - SB Admin</title>
        
        <link href="/css/styles.css" rel="stylesheet" /> 
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
    </head>
    <body class="sb-nav-fixed">
        <jsp:include page="../layout/header.jsp" />
        <div id="layoutSidenav">
            <jsp:include page="../layout/sidebar.jsp"/>
            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid px-4">
                        <h1 class="mt-4">Manage Poduct</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                            <li class="breadcrumb-item active">Product</li>    
                        </ol>
                            <div >
            <div class="col-md-6 col-12 mx-auto">
            <legend>Create new user</legend>

            <form:form action="/admin/user/create" method="post" modelAttribute="newUser">
                <div>
                <div> 
                    <label for="emailInput" class="form-label">Email</label>
                </div>

                <div>
                    <form:input placeholder="Confirm your email" type="text" id="emailInput" name="" class="form-control" 
                    path="email"/>
                </div>
            </div>

            <div>
                <div>
                    <label for="inputPassword" class="form-label">Password</label>
                </div>

                <div>
                    <form:input type="password" name="" id="inputPassword" class="form-control"
                    path="password"/>    
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
                    path="phone"/>
                </div>
            </div>

            <button type="submit" class="btn btn-success">Create</button> 
            </form:form>
            </div>
            
                            </div>
                    </div>
                </main>
                <jsp:include page="../layout/footer.jsp" />
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="/js/scripts.js"></script>
    </body>
</html>




