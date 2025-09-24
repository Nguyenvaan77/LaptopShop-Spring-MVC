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
                        <div class="container">
        <div class="col-12 mx-auto">
            <div class="d-flex justify-content-between">
                <h3>
                    Table users with user id = ${id}
                </h3>
            </div>
 
            <hr/>

            <div modelAttribute="user">
                <div class="col">
                        <img style="max-height: 150px;" src="${user.avatar}" alt="avatar preview" id="avatarPreview">
                </div>
                <div class="card" 60% >
                    <div class="card-header">
                        User information
                    </div>

                    <div class="row">
                        <div class="col">
                            <ul class="list-group list-group-flush">
                                <li class="list-group-item">Id: ${user.id}</li>
                                <li class="list-group-item">Email: ${user.email}</li>
                                <li class="list-group-item">Fullname: ${user.fullName}</li>
                                <li class="list-group-item">Address: ${user.address}</li>
                                <li class="list-group-item">Phone: ${user.phone}</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <a href="/admin/user" class="btn btn-success">Back</a>
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




