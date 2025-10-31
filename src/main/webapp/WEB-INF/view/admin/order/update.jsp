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
    
</head>
<body class="sb-nav-fixed">
    <jsp:include page="../layout/header.jsp" />
    <div id="layoutSidenav">
        <jsp:include page="../layout/sidebar.jsp"/>
        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Manage order</h1>
                    <ol class="breadcrumb mb-4">
                        <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                        <li class="breadcrumb-item active">Order</li>    
                    </ol>
                    
                    <div class="container">
                        <div class="col-md-6 col-12 mx-auto">
                            <legend>Update order</legend>

                            <form:form action="/admin/order/update" method="post" modelAttribute="updatedOrder">
                                <div style="display: none">
                                    <div class="col-md-6"> 
                                        <label for="idInput" class="form-label">Id</label>
                                    </div>

                                    <div class="col-md-6">
                                        <form:input placeholder="Your id" type="number" id="idInput" class="form-control" 
                                        path="id"/>
                                    </div>
                                </div>


                                <div class="row gx-3">
                                    
                                    <div class="col-md-6">
                                        <label for="selectFactory" class="form-label">Status</label>
                                        <form:select class="form-select" id="selectFactory" path = "status">
                                            <form:option value="PENDING">PENDING</form:option>
                                            <form:option value="SHIPPING">SHIPPING</form:option>
                                            <form:option value="SUCCESS">SUCCESS</form:option>
                                            <form:option value="CANCEL">CANCEL</form:option>
                                        </form:select>
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



    