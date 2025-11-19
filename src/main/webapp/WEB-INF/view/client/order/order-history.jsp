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
                    <table class="table">
                        <thead>
                          <tr>
                            <th scope="col">Products</th>
                            <th scope="col">Name</th>
                            <th scope="col">Price</th>
                            <th scope="col">Quantity</th>
                            <th scope="col">Total</th>
                            <th scope="col">Total</th>
                          </tr>
                        </thead>
                        <tbody>
                            <c:forEach var = "order" items = "${orders}" varStatus = "status">
                                <tr>
                                    <td >
                                        Order id = ${order.id}
                                    </td>

                                    <td colspan="3">
                                        Order at ${order.createdAt}
                                    </td>

                                    <td >
                                        Total = ${order.totalPrice}
                                    </td>

                                    <td>
                                        ${order.status}
                                    </td>
                                </tr>

                                <c:forEach var = "orderDetail" items = "${order.orderDetails}" >
                                    <c:set var="product" value="${orderDetail.product}" />

                                    <tr>
                                        <th scope="row">
                                            <div class="d-flex align-items-center">
                                                <img src="${product.image}" class="img-fluid me-5 rounded-circle" style="width: 80px; height: 80px;" alt="">
                                            </div>
                                        </th>
                                        <td>
                                            <a href="/product/${product.id}"><p class="mb-0 mt-4">${product.name}</p></a>
                                        </td>
                                        <td>
                                            <p class="mb-0 mt-4">${product.price} đ</p>
                                        </td>
                                        <td>
                                            <div class="input-group quantity mt-4" style="width: 100px;">
                                                <input type="text" class="form-control form-control-sm text-center border-0" 
                                                    value="${orderDetail.quantity}" 
                                                    cart-detail-id = "${orderDetail.id}"
                                                    cart-detail-price = "${product.price}"
                                                    cart-detail-index = "${status.index}">
                                            </div>
                                        </td>
                                        <td>
                                            <p class="mb-0 mt-4" cart-detail-id = "${orderDetail.id}">
                                                <fmt:formatNumber type = "number" />${orderDetail.price} đ
                                            </p>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:forEach> 
                            
                            
                        </tbody>
                    </table>
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