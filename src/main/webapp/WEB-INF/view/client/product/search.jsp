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

        <!-- Modal Search Start -->
        <div class="modal fade" id="searchModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-fullscreen">
                <div class="modal-content rounded-0">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Search by keyword</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body d-flex align-items-center">
                        <div class="input-group w-75 mx-auto d-flex">
                            <input type="search" class="form-control p-3" placeholder="keywords" aria-describedby="search-icon-1">
                            <span id="search-icon-1" class="input-group-text p-3"><i class="fa fa-search"></i></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Modal Search End -->


        <!-- Single Page Header start -->
        <div class="container-fluid page-header py-5">
            <h1 class="text-center text-white display-6">Shop</h1>
            <ol class="breadcrumb justify-content-center mb-0">
                <li class="breadcrumb-item"><a href="#">Home</a></li>
                <li class="breadcrumb-item"><a href="#">Pages</a></li>
                <li class="breadcrumb-item active text-white">Shop</li>
            </ol>
        </div>
        <!-- Single Page Header End -->


        <!-- Fruits Shop Start-->
        <div class="container-fluid fruite py-5">
            <div class="container py-5">
                <h1 class="mb-4">Fresh fruits shop</h1>
                <div class="row g-4">
                    <div class="col-lg-12">
                        <div class="row g-4">
                            <div class="col-xl-3">
                                <div class="input-group w-100 mx-auto d-flex">
                                    <input type="search" class="form-control p-3" placeholder="keywords" aria-describedby="search-icon-1">
                                    <span id="search-icon-1" class="input-group-text p-3"><i class="fa fa-search"></i></span>
                                </div>
                            </div>
                            <div class="col-6"></div>
                            
                        </div>
                        <div class="row g-4">
                            <div class="col-lg-3">
                                <div class="row g-4">

                                    <div class="col-lg-12" id="factoryFilter">
                                        <div class="mb-3">
                                            <h4>Hãng sản xuất</h4>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="checkbox" value="lenovo" id="factoryCheck1">
                                                <label class="form-check-label" for="factoryCheck1">
                                                    Lenovo
                                                </label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" value="asus" id="factoryCheck2">
                                                <label class="form-check-label" for="factoryCheck2">
                                                    Asus
                                                </label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" value="dell" id="factoryCheck3">
                                                <label class="form-check-label" for="factoryCheck3">
                                                    Dell
                                                </label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" value="acer" id="factoryCheck4">
                                                <label class="form-check-label" for="factoryCheck4">
                                                    Acer
                                                </label>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-lg-12" id="targetFilter">
                                        <div class="mb-3">
                                            <h4>Mục đích sử dụng</h4>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" value="gaming" id="targetCheck1">
                                                <label class="form-check-label" for="targetCheck1">
                                                    Gaming
                                                </label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" value="mong-nhe" id="targetCheck2">
                                                <label class="form-check-label" for="targetCheck2">
                                                    Mỏng nhẹ
                                                </label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" value="van-phong" id="targetCheck3">
                                                <label class="form-check-label" for="targetCheck3">
                                                    Văn phòng
                                                </label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" value="do-hoa" id="targetCheck4">
                                                <label class="form-check-label" for="targetCheck4">
                                                    Thiết kế đồ họa
                                                </label>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-lg-12" id="priceFilter">
                                        <div class="mb-3">
                                            <h4>Mức giá</h4>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" value="0-toi-5-trieu" id="priceCheck1">
                                                <label class="form-check-label" for="priceCheck1">
                                                    Dưới 5 triệu
                                                </label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" value="5-toi-10-trieu" id="priceCheck2">
                                                <label class="form-check-label" for="priceCheck2">
                                                    từ 5 - 10 triệu
                                                </label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" value="10-toi-15-trieu" id="priceCheck3">
                                                <label class="form-check-label" for="priceCheck3">
                                                    Từ 10 - 15 triệu
                                                </label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" value="15-toi-30-trieu" id="priceCheck4">
                                                <label class="form-check-label" for="priceCheck4">
                                                    Trên 15 triệu
                                                </label>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-lg-12">
                                        <div class="mb-3">
                                            <h4>Sắp xếp</h4>
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="radio-sort" id="radioDefault1" value="giam-dan">
                                                <label class="form-check-label" for="radioDefault1">
                                                    Giá giảm dần
                                                </label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="radio-sort" id="radioDefault2" value="tang-dan">
                                                <label class="form-check-label" for="radioDefault2">
                                                    Giá tăng dần
                                                </label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="radio-sort" id="radioDefault3" value="khong" checked>
                                                <label class="form-check-label" for="radioDefault2">
                                                    Bất kì
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                    
                                     <div class="d-flex justify-content-center my-4">
                                            <button class="btn border border-secondary px-4 py-3 rounded-pill text-primary w-100"
                                                    id="btnFilter"
                                                    >
                                                    Lọc sản phẩm
                                            </button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-9">
                                <div class="row g-4 justify-content-center">
                                    <c:forEach var = "eachLaptop" items = "${products}">
                                        <div class="col-md-6 col-lg-6 col-xl-4">
                                            <div class="rounded position-relative fruite-item">
                                                <div class="fruite-img">
                                                    <img src="${eachLaptop.image}" class="img-fluid w-100 rounded-top" alt="">
                                                </div>
                                                <div class="text-white bg-secondary px-3 py-1 rounded position-absolute" style="top: 10px; left: 10px;">Laptop</div>
                                                <div class="p-4 border border-secondary border-top-0 rounded-bottom">
                                                    <h4>${eachLaptop.name}</h4>
                                                    <p>${eachLaptop.shortDesc}</p>
                                                    <div class="d-flex justify-content-between flex-lg-wrap">
                                                        <p class="text-dark fs-5 fw-bold mb-0">$ ${eachLaptop.price}</p>
                                                        <a href="#" class="btn border border-secondary rounded-pill px-3 text-primary"><i class="fa fa-shopping-bag me-2 text-primary"></i> Add to cart</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>

                                    <c:if test = "${totalPages == 0}">
                                        <div>Không tìm thấy sản phẩm</div>
                                    </c:if>
                                    
                                    <c:if test="${totalPages > 0}">
                                        <div class="col-12">
                                            <div class="pagination d-flex justify-content-center mt-5">
                                                <a href="#" class="rounded">&laquo;</a>
                                                <c:forEach begin="0" end="${totalPages-1}" varStatus="loop">
                                                    <a href="#" class="rounded ${loop.index eq currentPage ? 'active' : ''}">${loop.index + 1}</a>
                                                </c:forEach>
                                                <a href="#" class="rounded">&raquo;</a>
                                            </div>
                                        </div>
                                    </c:if>
                                    
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Fruits Shop End-->

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