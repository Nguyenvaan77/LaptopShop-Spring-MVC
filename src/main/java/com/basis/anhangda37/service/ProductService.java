package com.basis.anhangda37.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.basis.anhangda37.config.SecurityConfiguration;
import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.Order;
import com.basis.anhangda37.domain.OrderDetail;
import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.domain.dto.ProductCriteriaDto;
import com.basis.anhangda37.repository.CartDetailRepository;
import com.basis.anhangda37.repository.CartRepository;
import com.basis.anhangda37.repository.OrderDetailRepository;
import com.basis.anhangda37.repository.OrderRepository;
import com.basis.anhangda37.repository.ProductRepository;
import com.basis.anhangda37.repository.UserRepository;
import com.basis.anhangda37.service.specification.ProductSpec;

import jakarta.servlet.http.HttpSession;

@Service
public class ProductService {

    private final SecurityConfiguration securityConfiguration;

    private final CartService cartService;

    private final CartDetailService cartDetailService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;

    public ProductService(UserRepository userRepository, ProductRepository productRepository,
            CartRepository cartRepository, CartDetailRepository cartDetailRepository, OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository, CartDetailService cartDetailService, CartService cartService,
            SecurityConfiguration securityConfiguration) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.cartDetailService = cartDetailService;
        this.cartService = cartService;
        this.securityConfiguration = securityConfiguration;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public Page<Product> getAllProduct(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> getAllProduct(String name, Pageable pageable) {
        return productRepository.findAll(ProductSpec.nameLike(name), pageable);
    }

    public void deleteProductById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
    }

    public long countProduct() {
        return productRepository.count();
    }

    public void handleProductToCart(HttpSession session, String gmailUser, Long productId, Long quantityToAdd) {
        User user = userRepository.findByEmail(gmailUser);
        Cart cart = cartRepository.findByUser(user);
        Product product = productRepository.findById(productId).orElse(null);

        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setSum(0);
            cartRepository.save(newCart);

            CartDetail newCartDetail = new CartDetail();
            newCartDetail.setCart(newCart);
            newCartDetail.setQuantity(quantityToAdd);
            newCartDetail.setProduct(product);

            newCart.addCartDetail(newCartDetail);
            cartRepository.save(newCart);
            session.setAttribute("sum", newCart.getSum());
            return;
        }

        CartDetail existCartDetail = cartDetailRepository.findByCartAndProduct(cart, product);
        if (existCartDetail == null) {
            CartDetail newCartDetail = new CartDetail();
            newCartDetail.setProduct(product);
            newCartDetail.setQuantity(quantityToAdd);
            newCartDetail.setCart(cart);
            cart.addCartDetail(newCartDetail);
            cartDetailRepository.save(newCartDetail);
            session.setAttribute("sum", cart.getSum());
            return;
        }

        existCartDetail.setQuantity(existCartDetail.getQuantity() + quantityToAdd);
        cartDetailRepository.save(existCartDetail);
    }

    @Transactional
    public String handleCheckOut(String email, HttpSession session, String receiverName, String receiverAddress,
            String receiverPhone, Double totalPayment, List<CartDetail> cartDetails) {
        handleProductBeforeCheckout(cartDetails);
        String orderCode = handlePlaceOrder(email, session, receiverName, receiverAddress, receiverPhone, totalPayment);
        return orderCode;
    }

    // Phương thức lọc cho tìm kiếm
    public Page<Product> getAllProductsByNameFilter(String name, Pageable page) {
        return productRepository.findAll(ProductSpec.nameLike(name), page);
    }

    public Page<Product> getAllProductsByListFactoryFilter(List<String> listFactorys, Pageable page) {
        return productRepository.findAll(ProductSpec.belongListFactory(listFactorys), page);
    }

    public Page<Product> getAllProductsByRangePriceFilter(String rangePriceString, Pageable page) {
        Double minPrice = this.getMinPriceFromRangeString(rangePriceString);
        Double maxPrice = this.getMaxPriceFromRangeString(rangePriceString);

        return productRepository.findAll(ProductSpec.rangePrice(minPrice, maxPrice), page);
    }

    public Page<Product> getAllProductsByListRangePriceFilter(List<String> listRangePriceString, Pageable page) {
        List<Double[]> listPriceThreshold = handlePriceThreshold(listRangePriceString);
        return productRepository.findAll(ProductSpec.listRangePrice(listPriceThreshold), page);
    }

    public Page<Product> fetchProductWithSpecification(Pageable page, ProductCriteriaDto productCriteriaDto) {
        Specification<Product> combinedSpec = Specification.allOf();

        if (productCriteriaDto.getFactory() != null && productCriteriaDto.getFactory().isPresent()) {
            Specification<Product> currentSpecs = ProductSpec.belongListFactory(productCriteriaDto.getFactory().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }

        if (productCriteriaDto.getTarget() != null && productCriteriaDto.getTarget().isPresent()) {
            Specification<Product> currentSpecs = ProductSpec.belongListTarget(productCriteriaDto.getTarget().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }

        if (productCriteriaDto.getPrice() != null && productCriteriaDto.getPrice().isPresent()) {
            Specification<Product> currentSpecs = ProductSpec
                    .listRangePrice(handlePriceThreshold(productCriteriaDto.getPrice().get()));
            combinedSpec = combinedSpec.and(currentSpecs);
        }

        if (productCriteriaDto.getSort() == null
                || (!productCriteriaDto.getSort().isPresent() || productCriteriaDto.getSort().get().equals("khong"))) {
            return this.productRepository.findAll(combinedSpec, page);
        } else {
            String sortString = productCriteriaDto.getSort().get();
            Sort sort;
            if (sortString.equals("tang-dan")) {
                sort = Sort.by(Sort.Direction.ASC, "price");
            } else {
                sort = Sort.by(Sort.Direction.DESC, "price");
            }
            Pageable sortedPageable = PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);
            return this.productRepository.findAll(combinedSpec, sortedPageable);
        }
    }

    /*
     * Đầu vào là chuỗi "10-toi-15-trieu"
     * Split dựa trên kí tự "-"
     * Chuỗi trả về có dạng String[] = {"10", "toi", "15", "trieu"}
     * Kết quả: min là String[0]="10", max là String [2] ="15"
     */
    private String[] handleRangePrice(String rangePriceString) {
        return rangePriceString.split("-");
    }

    private Double getMinPriceFromRangeString(String rangePriceString) {
        return Double.parseDouble(handleRangePrice(rangePriceString)[0]);
    }

    private Double getMaxPriceFromRangeString(String rangePriceString) {
        return Double.parseDouble(handleRangePrice(rangePriceString)[2]);
    }

    // Hết

    private void handleProductBeforeCheckout(List<CartDetail> cartDetails) {
        if (cartDetails == null || cartDetails.isEmpty()) {
            return;
        }

        for (CartDetail cartDetailDto : cartDetails) {
            CartDetail fetchCartDetail = cartDetailRepository.findById(cartDetailDto.getId()).get();
            Product product = fetchCartDetail.getProduct();
            Long quantityToSold = cartDetailDto.getQuantity();
            fetchCartDetail.setQuantity(quantityToSold);
            product.setQuantity(product.getQuantity() - quantityToSold);
            productRepository.save(product);
            cartDetailRepository.save(fetchCartDetail);
        }
    }

    private String handlePlaceOrder(String email, HttpSession session, String receiverName, String receiverAddress,

            String receiverPhone, Double totalPayment) {
        // User user = userRepository.findByEmail(email);
        // Cart cart = user.getCart();

        // cart.removeAllCartDetail();
        // cartRepository.save(cart);
        // user.removeCart();
        // cart.setUser(null);
        // cartRepository.deleteById(cart.getId());

        User managedUser = userRepository.findByEmail(email);
        Order order = new Order();
        order.setReceiverAddress(receiverAddress);
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setUser(managedUser);
        order.setTotalPrice(totalPayment);

        Cart cart = cartRepository.findByUser(managedUser);
        List<CartDetail> cartDetails = cartDetailRepository.findByCart(cart);

        List<OrderDetail> orderDetails = new ArrayList<>();

        for (CartDetail cartDetail : cartDetails) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setProduct(cartDetail.getProduct());
            orderDetail.setPrice(cartDetail.getProduct().getPrice() *
                    cartDetail.getQuantity());
            // Tăng số lượng sản phẩm đã bán ~ ++ sold (Product)
            cartDetail.getProduct().setSold(cartDetail.getProduct().getSold() +
                    cartDetail.getQuantity());
            orderDetail.setOrder(order);
            orderDetails.add(orderDetail);
        }

        order.setOrderDetails(orderDetails);
        orderRepository.save(order);

        cart.removeAllCartDetail();
        managedUser.removeCart();
        cart.setUser(null);
        cartRepository.deleteById(cart.getId());

        session.setAttribute("sum", 0);
        return String.valueOf(order.getId());
    }

    private List<Double[]> handlePriceThreshold(List<String> listRangePriceString) {
        List<Double[]> listPriceThreshold = new ArrayList<>();

        listRangePriceString.forEach(item -> {
            Double[] foo = new Double[2];
            foo[0] = this.getMinPriceFromRangeString(item);
            foo[1] = this.getMaxPriceFromRangeString(item);
            listPriceThreshold.add(foo);
        });

        return listPriceThreshold;
    }

}
