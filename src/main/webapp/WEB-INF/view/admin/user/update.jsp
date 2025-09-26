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

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script>
        $(document).ready(() => { 
            const avatarFile = $("#avatarFile");
            avatarFile.change(function (e) {
                const imgURL = URL.createObjectURL(e.target.files[0]);
                $("#avatarPreview").removeAttr("src");
                $("#avatarPreview").attr("src", imgURL);
            });
        });
    </script>
</head>
<body class="sb-nav-fixed">
    <jsp:include page="../layout/header.jsp" />
    <div id="layoutSidenav">
        <jsp:include page="../layout/sidebar.jsp"/>
        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Manage User</h1>
                    <ol class="breadcrumb mb-4">
                        <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                        <li class="breadcrumb-item active">User</li>    
                    </ol>
                    
                    <div class="container">
                        <div class="col-md-6 col-12 mx-auto">
                            <legend>Update user</legend>

                            <form:form action="/admin/user/update" method="post" modelAttribute="newUser" enctype="multipart/form-data">
                                <div style="display: none">
                                    <div class="col-md-6"> 
                                        <label for="idInput" class="form-label">Id</label>
                                    </div>

                                    <div class="col-md-6">
                                        <form:input placeholder="Your id" type="text" id="idInput" class="form-control" 
                                        path="id" />
                                    </div>
                                </div>

                                <div class="row gx-3">
                                    <div class="col-md-6">
                                        <label for="emailInput" class="form-label">Email</label>
                                        <form:input placeholder="Confirm your email" type="text" id="emailInput" class="form-control" 
                                        path="email" disabled="true"/>
                                    </div>

                                    <div class="col-md-6">
                                        <label for="inputName" class="form-label">Fullname</label>
                                        <form:input type="text" id="inputName" class="form-control"
                                        path="fullName" />
                                    </div>
                                </div>

                                <div class="row gx-3">
                                    <div class="col-md-6">
                                        <label for="inputAddress" class="form-label">Address</label>
                                        <form:input type="text" id="inputAddress" class="form-control"
                                        path="address"/>
                                    </div>

                                    <div class="col-md-6">
                                        <label for="inputPhoneNumber" class="form-label">Phone number</label>
                                        <form:input type="number" id="inputPhoneNumber" class="form-control"
                                        path="phone"/>
                                    </div>
                                </div>

                                <div class="row gx-3">
                                    <div class="col-md-6">
                                        <label for="selectRole" class="form-label">Role:</label>
                                        <form:select class="form-select" id="selectRole" path = "role.name">
                                            <form:option value="USER">USER</form:option>
                                            <form:option value="ADMIN">ADMIN</form:option>
                                        </form:select>
                                    </div>

                                    <div class="col-md-6">
                                        <label for="avatarFile" class="form-label">Upload file</label>
                                        <input name="hoidanitFile" type="file" id="avatarFile" class="form-control" accept = ".png, .jpg, .jpeg"
                                        />
                                    </div>
                                    <div class="col-md-6">
                                        <img style="max-height: 250px; display: block;" alt="avatar preview" id="avatarPreview" src="${newUser.avatar}">
                                    </div>
                                    
                                </div>

                                <button type="submit" class="btn btn-warning">Update</button> 
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



    