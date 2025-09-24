<%@page contentType="text/html" pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
                        <div class="mt-5">
                            <div class="col-12 mx-auto">
                                <div class="d-flex justify-content-between">
                                    <h3>
                                        Table Products
                                    </h3>
                                    <div class="btn btn-primary">
                                        <a href="/admin/product/create">Add new product</a>
                                    </div>
                                </div>

                                <hr/>
                                <table class="table table-bordered">
                                    <thead class="table-active">
                                        <tr>
                                            <th scope="col">ID</th>
                                            <th scope="col">Name</th>
                                            <th scope="col">Factory</th>
                                            <th scope="col">Price</th>
                                            <th scope="col">Quantity</th>
                                            <th scope="col">Target</th>
                                            <th scope="col">Description</th>
                                            <th scope="col">Action</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <c:forEach var ="eachLaptop" items = "${laptops}">
                                            <tr>
                                                <th scope="row">${eachLaptop.id}</th>
                                                <td>${eachLaptop.name}</td>
                                                <td>${eachLaptop.factory}</td>
                                                <td>${eachLaptop.price}</td>
                                                <td>${eachLaptop.quantity}</td>
                                                <td>${eachLaptop.target}</td>
                                                <td>${eachLaptop.shortDesc}</td>
                                                <td>
                                                    <div class="button">
                                                    <a href="/admin/product/${eachLaptop.id}" class = "btn btn-primary">View</a>
                                                    <a href="/admin/product/update/${eachLaptop.id}" class = "btn btn-warning">Update</a>
                                                    <a href="/admin/product/delete/${eachLaptop.id}" class = "btn btn-danger">Delete</a>
                                                </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </main>
                <jsp:include page="../layout/footer.jsp" />
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="js/scripts.js"></script>
    </body>
</html>
