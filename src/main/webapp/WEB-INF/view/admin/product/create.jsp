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
                    <h1 class="mt-4">Manage Product</h1>
                    <ol class="breadcrumb mb-4">
                        <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                        <li class="breadcrumb-item active">Product</li>    
                    </ol>

                    <div class="container">
                        <div class="col-md-6 col-12 mx-auto">
                            <legend>Create new product</legend>

                            <form:form action="/admin/product/create" method="post" enctype="multipart/form-data" modelAttribute="newProduct">
                            
                            <div class="row gx-3">
                                <!-- Name -->
                                <c:set var = "errorName">
                                    <form:errors path="name" cssClass = "invalid-feedback"/>
                                </c:set>
                                <div class="col-md-6">
                                    <label for="nameInput" class="form-label">Name</label>
                                    <form:input type="text" 
                                                id="nameInput" class="form-control ${not empty errorName ? 'is-invalid' : ''}" 
                                                path="name"/>
                                    ${errorName}
                                </div>

                                <!-- Price -->
                                <div class="col-md-6">
                                    <c:set var = "errorPrice">
                                        <form:errors path="price" cssClass = "invalid-feedback"/>
                                    </c:set>
                                    <label for="inputPrice" class="form-label">Price</label>
                                    <form:input type="number" id="inputPrice" 
                                                class="form-control ${not empty errorPrice ? 'is-invalid' : ''}"
                                                path="price"/>    
                                    ${errorPrice}
                                </div>
                            </div>

                            <div class="row gx-3">
                                <div class="md-10">
                                    <label for="inputDetailDesc" class="form-label">Detail desc</label>
                                    <form:input type="text" name="" id="inputDetailDesc" class="form-control"
                                    path="detailDesc"/>
                                </div>
                            </div>

                            <div class="row gx-3">
                                <div class="col-md-6">
                                    <label for="inputShortDesc" class="form-label">Short desc</label>
                                    <form:input type="text" name="" id="inputShortDesc" class="form-control"
                                    path="shortDesc"/>
                                </div>

                                <div class="col-md-6">
                                    <c:set var = "errorQuantity">
                                        <form:errors path="quantity" cssClass = "invalid-feedback"/>
                                    </c:set>
                                    <label for="inputQuantity" class="form-label">Quantity</label>
                                    <form:input type="number" name="" id="inputQuantity" class="form-control ${not empty errorQuantity ? 'is-invalid' : ''}"
                                    path="quantity"/>
                                </div>
                            </div>

                            <div class="row gx-3">
                                <div class="col-md-6">
                                    <label for="selectFactory" class="form-label">Factory</label>
                                    <form:select class="form-select" id="selectFactory" path = "factory">
                                        <form:option value="USER">APPLE</form:option>
                                        <form:option value="ASUS">ASUS</form:option>
                                        <form:option value="LENOVO">LENOVO</form:option>
                                        <form:option value="SAMSUNG">SAMSUNG</form:option>
                                    </form:select>
                                </div>

                                <div class="col-md-6">
                                    <label for="selectTarget" class="form-label">Target</label>
                                    <form:select class="form-select" id="selectTarget" path = "target">
                                        <form:option value="Gaming">Gaming</form:option>
                                        <form:option value="Office">Office</form:option>
                                        <form:option value="Personal">Personal</form:option>
                                        <form:option value="Organization">Organization</form:option>
                                    </form:select>
                                </div>
                            </div>

                            <div class="row gx-3">
                                <div class="col-md-6">
                                    <label for="avatarFile" class="form-label">Upload file</label>
                                    <input name="avatarProduct" type="file" id="avatarFile" class="form-control" accept = ".png, .jpg, .jpeg"
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




