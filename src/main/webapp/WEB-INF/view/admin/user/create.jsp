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
                $("#avatarPreview").attr("src", imgURL);
                $("#avatarPreview").css({"display":"block"});
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
                    <h1 class="mt-4">Manage Poduct</h1>
                    <ol class="breadcrumb mb-4">
                        <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                        <li class="breadcrumb-item active">Product</li>    
                    </ol>

                    <div class="container">
                        <div class="col-md-6 col-12 mx-auto">
                            <legend>Create new user</legend>

                            <form:form action="/admin/user/create" method="post" enctype="multipart/form-data" modelAttribute="newUser">
                            
                            <div class="row gx-3">  
                                <c:set var = "errorEmail">
                                    <form:errors path ="email" cssClass = "invalid-feedback"></form:errors>
                                </c:set>
                                <!-- Email -->
                                <div class="col-md-6">
                                    <label for="inputEmail" class="form-label">Email</label>
                                    <form:input placeholder="Confirm your email" type="text" 
                                                id="inputEmail" 
                                                class="form-control ${not empty errorEmail ? 'is-invalid' : ''}" 
                                                path="email"/>
                                    ${errorEmail}
                                </div>

                                <!-- Password -->
                                <div class="col-md-6">
                                    <c:set var = "errorPassword">
                                        <form:errors path = "password" cssClass = "invalid-feedback"/>
                                    </c:set>

                                    <label for="inputPassword" class="form-label">Password</label>
                                    <form:input type="password" id="inputPassword" 
                                                class="form-control ${not empty errorPassword ? 'is-invalid' : ''}"
                                                path="password"
                                                />    
                                    ${errorPassword}
                                </div>
                            </div>

                            <div class="row gx-3">
                                <c:set var ="errorName">
                                    <form:errors path = "fullName" cssClass="invalid-feedback"/>
                                </c:set>

                                <div class="col-md-6">
                                    <label for="inputName" class="form-label">Fullname</label>
                                    <form:input type="text"
                                                id="inputName" 
                                                class="form-control ${not empty errorName ? 'is-invalid' : ''}"
                                                path="fullName"/>
                                    ${errorName}
                                </div>

                                <div class="col-md-6">
                                    <label for="inputAdress" class="form-label">Address</label>
                                    <form:input type="text" name="" id="inputAdress" class="form-control"
                                    path="address"/>
                                </div>
                            </div>

                                

                            <div class="row gx-3">
                                <div class="col-md-6">
                                    <label for="inputPhoneNumber" class="form-label">Phone number</label>
                                    <form:input type="number" name="" id="inputPhoneNumber" class="form-control"
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
                                    <img style="max-height: 250px; display: none;" alt="avatar preview" id="avatarPreview">
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




