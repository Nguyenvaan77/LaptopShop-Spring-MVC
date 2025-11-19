<%@page contentType="text/html" pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <title>Fruitables - Vegetable Website Template</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">

        <!-- Google Web Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Raleway:wght@600;800&display=swap" rel="stylesheet"> 

        <!-- Icon Font Stylesheet -->
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

        <!-- Libraries Stylesheet -->
        <link href="/client/lib/lightbox/css/lightbox.min.css" rel="stylesheet">
        <link href="/client/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">


        <!-- Customized Bootstrap Stylesheet -->
        <link href="/client/css/bootstrap.min.css" rel="stylesheet">

        <!-- Template Stylesheet -->
        <link href="/client/css/style.css" rel="stylesheet">
    </head>

    <body>
        <jsp:include page="../layout/header.jsp" />

        


        <!-- Cart Page Start -->
        <div class="container-fluid py-5">
            <div class="container py-5">
                <div class="table-responsive">
                    
                    <div class="container">
                        <div class="col-md-6 col-12 mx-auto">
                            <legend>Update user</legend>

                            <form:form action="/user/information" method="post" modelAttribute="newUser" enctype="multipart/form-data">
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
                
            </div>
        </div>
        <!-- Cart Page End -->

        <!-- Back to Top -->
        <a href="#" class="btn btn-primary border-3 border-primary rounded-circle back-to-top"><i class="fa fa-arrow-up"></i></a>   

        
    <!-- JavaScript Libraries -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="/client/lib/easing/easing.min.js"></script>
    <script src="/client/lib/waypoints/waypoints.min.js"></script>
    <script src="/client/lib/lightbox/js/lightbox.min.js"></script>
    <script src="/client/lib/owlcarousel/owl.carousel.min.js"></script>

    <!-- Template Javascript -->
    <script src="/client/js/main.js"></script>
    </body>

</html>