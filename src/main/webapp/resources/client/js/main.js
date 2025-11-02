(function ($) {
    "use strict";

    // Spinner
    var spinner = function () {
        setTimeout(function () {
            if ($('#spinner').length > 0) {
                $('#spinner').removeClass('show');
            }
        }, 1);
    };
    spinner(0);


    // Fixed Navbar
    $(window).scroll(function () {
        if ($(window).width() < 992) {
            if ($(this).scrollTop() > 55) {
                $('.fixed-top').addClass('shadow');
            } else {
                $('.fixed-top').removeClass('shadow');
            }
        } else {
            if ($(this).scrollTop() > 55) {
                $('.fixed-top').addClass('shadow').css('top', 0);
            } else {
                $('.fixed-top').removeClass('shadow').css('top', 0);
            }
        } 
    });
    
    
   // Back to top button
   $(window).scroll(function () {
    if ($(this).scrollTop() > 300) {
        $('.back-to-top').fadeIn('slow');
    } else {
        $('.back-to-top').fadeOut('slow');
    }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');
        return false;
    });


    // Testimonial carousel
    $(".testimonial-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 2000,
        center: false,
        dots: true,
        loop: true,
        margin: 25,
        nav : true,
        navText : [
            '<i class="bi bi-arrow-left"></i>',
            '<i class="bi bi-arrow-right"></i>'
        ],
        responsiveClass: true,
        responsive: {
            0:{
                items:1
            },
            576:{
                items:1
            },
            768:{
                items:1
            },
            992:{
                items:2
            },
            1200:{
                items:2
            }
        }
    });


    // vegetable carousel
    $(".vegetable-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 1500,
        center: false,
        dots: true,
        loop: true,
        margin: 25,
        nav : true,
        navText : [
            '<i class="bi bi-arrow-left"></i>',
            '<i class="bi bi-arrow-right"></i>'
        ],
        responsiveClass: true,
        responsive: {
            0:{
                items:1
            },
            576:{
                items:1
            },
            768:{
                items:2
            },
            992:{
                items:3
            },
            1200:{
                items:4
            }
        }
    });


    // Modal Video
    $(document).ready(function () {
        var $videoSrc;
        $('.btn-play').click(function () {
            $videoSrc = $(this).data("src");
        });
        console.log($videoSrc);

        $('#videoModal').on('shown.bs.modal', function (e) {
            $("#video").attr('src', $videoSrc + "?autoplay=1&amp;modestbranding=1&amp;showinfo=0");
        })

        $('#videoModal').on('hide.bs.modal', function (e) {
            $("#video").attr('src', $videoSrc);
        })
    });

    //Hàm ready giữ trạng thái các ô checkbox khi reload lại trang product
    $(document).ready(function () {
        const currentPath = window.location.pathname;
        if (currentPath !== '/product') return; // 🔒 chỉ chạy ở /shop

        const urlParams = new URLSearchParams(window.location.search);
        const factories = urlParams.get('factory')?.split(',') || [];
        const targets = urlParams.get('target')?.split(',') || [];
        const prices = urlParams.get('price')?.split(',') || [];
        const sort = urlParams.get('sort');

        factories.forEach(f => {
            $(`#factoryFilter .form-check-input[value="${f}"]`).prop('checked', true);
        });
        targets.forEach(t => {
            $(`#targetFilter .form-check-input[value="${t}"]`).prop('checked', true);
        });
        prices.forEach(p => {
            $(`#priceFilter .form-check-input[value="${p}"]`).prop('checked', true);
        });
        if (sort) {
            $(`input[name="radio-sort"][value="${sort}"]`).prop('checked', true);
        }
    });



    // // Product Quantity
    // $('.quantity button').on('click', function () {
    //     var button = $(this);
    //     var oldValue = button.parent().parent().find('input').val();
    //     if (button.hasClass('btn-plus')) {
    //         var newVal = parseFloat(oldValue) + 1;
    //     } else {
    //         if (oldValue > 0) {
    //             var newVal = parseFloat(oldValue) - 1;
    //         } else {
    //             newVal = 0;
    //         }
    //     }
    //     button.parent().parent().find('input').val(newVal);
    // });

    $('.quantity button').on('click', function () {
    let change = 0;

    const button = $(this);
    const input = button.parent().parent().find('input');
    const oldValue = parseFloat(input.val());
    const quantityInStock = input.attr("cart-detail-quantity-in-stock");

    let newVal = oldValue;

    if (button.hasClass('btn-plus')) {
        if(newVal < quantityInStock) {
            newVal = oldValue + 1;
            change = 1; // tăng thì cộng vào tổng
        }
    } else {
        if (oldValue > 1) {
            newVal = oldValue - 1;
            change = -1; // giảm thì trừ khỏi tổng
        } else {
            newVal = 1;
        }
    }

    const plusBtn = button.parent().parent().find('.btn-plus');
    const minusBtn = button.parent().parent().find('.btn-minus');

    if(newVal >= quantityInStock) {
        plusBtn.prop('disabled', true);
    } else {
        plusBtn.prop('disabled', false);
    }

    if(newVal < 2) {
        minusBtn.prop('disabled', true);
    } else {
        minusBtn.prop('disabled', false);
    }
    

    input.val(newVal);

    const index = input.attr("cart-detail-index");
    const el = document.getElementById(`cartDetails${index}.quantity`);
    $(el).val(newVal);

    const price = parseFloat(input.attr("cart-detail-price"));
    const id = input.attr("cart-detail-id");

    const priceElement = $(`p[cart-detail-id='${id}']`);
    if (priceElement.length) {
        const newPrice = price * newVal;
        priceElement.text(formatCurrency(newPrice.toFixed(0)) + " đ");
    }

    const totalPriceElement = $(`p[cart-total-price]`);

    if (totalPriceElement.length) {
        const currentTotal = parseFloat(totalPriceElement.first().attr("cart-total-price"));
        let newTotal = currentTotal + (change * price);

        // Đảm bảo không bị NaN
        if (isNaN(newTotal)) newTotal = 0;

        totalPriceElement.each(function (index, element) {
            $(element).text(formatCurrency(newTotal.toFixed(0)) + " đ");
            $(element).attr("cart-total-price", newTotal);
        });
    }
});

    

$('.quantity.onProductDetail button').on('click', function () {
    let change = 0;

    const button = $(this);
    const input = button.parent().parent().find('input');
    const oldValue = parseFloat(input.val());
    const quantityInStock = input.attr("product-quantity-in-stock");

    let newVal = oldValue;

    if (button.hasClass('btn-plus')) {
        if(newVal < quantityInStock) {
            newVal = oldValue + 1;
            change = 1; // tăng thì cộng vào tổng
        }
    } else {
        if (oldValue > 1) {
            newVal = oldValue - 1;
            change = -1; // giảm thì trừ khỏi tổng
        } else {
            newVal = 1;
        }
    }

    const plusBtn = button.parent().parent().find('.btn-plus');
    const minusBtn = button.parent().parent().find('.btn-minus');

    if(newVal >= quantityInStock) {
        plusBtn.prop('disabled', true);
    } else {
        plusBtn.prop('disabled', false);
    }

    if(newVal < 2) {
        minusBtn.prop('disabled', true);
    } else {
        minusBtn.prop('disabled', false);
    }
    

    input.val(newVal);

    const el = document.getElementById(`quantity`);
    $(el).val(newVal);
});

// ---- Hàm format tiền tệ ----
function formatCurrency(value) {
    const formatter = new Intl.NumberFormat('vi-VN', {
        style: 'decimal',
        minimumFractionDigits: 0,
    });

    let formatted = formatter.format(value);
    formatted = formatted.replace(/\./g, ',');
    return formatted;
}

$('#btnFilter').click(function (event) {
    event.preventDefault();

    let factoryArr = [];
    let targetArr = [];
    let priceArr = [];

    $("#factoryFilter .form-check-input:checked").each(function () {
        factoryArr.push($(this).val());
    });

    $("#targetFilter .form-check-input:checked").each(function () {
        targetArr.push($(this).val());
    });

    $("#priceFilter .form-check-input:checked").each(function () {
        priceArr.push($(this).val());
    });

    let sortValue = $('input[name="radio-sort"]:checked').val();

    const currentUrl = new URL(window.location.href);
    const searchParams = currentUrl.searchParams;

    searchParams.set('page', '0');
    searchParams.set('sort', sortValue);

    if(factoryArr.length > 0) {
        searchParams.set('factory', factoryArr.join(','));
    }

    if(targetArr.length > 0) {
        searchParams.set('target', targetArr.join(','));
    }

    if(priceArr.length > 0) {
        searchParams.set('price', priceArr.join(','));
    }

    window.location.href = currentUrl.toString();
});



})(jQuery);

