package com.basis.anhangda37.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.domain.dto.CartDetailDto;
import com.basis.anhangda37.repository.CartDetailRepository;
import com.basis.anhangda37.service.iface.ICartDetailService;

/**
 * Service class for CartDetail-related operations.
 * Handles shopping cart item management and conversion to DTOs.
 * Implements clean separation of concerns with proper logging.
 */
@Service
public class CartDetailService implements ICartDetailService {

    private static final Logger logger = LoggerFactory.getLogger(CartDetailService.class);

    private final CartDetailRepository cartDetailRepository;

    /**
     * Constructs a CartDetailService with required repositories.
     */
    public CartDetailService(CartDetailRepository cartDetailRepository) {
        this.cartDetailRepository = cartDetailRepository;
    }

    /**
     * Retrieves all cart details for a specific cart.
     * @param cart The cart
     * @return A list of cart details
     */
    @Override
    public List<CartDetail> getCartDetailsByCart(Cart cart) {
        logger.debug("Fetching cart details for cart id: {}", cart.getId());
        return cartDetailRepository.findByCart(cart);
    }

    /**
     * Retrieves a cart detail by ID.
     * @param id The cart detail ID
     * @return An Optional containing the cart detail, if found
     */
    @Override
    public Optional<CartDetail> getCartDetailById(Long id) {
        logger.debug("Fetching cart detail with id: {}", id);
        return cartDetailRepository.findById(id);
    }

    /**
     * Deletes a cart detail.
     * @param cartDetail The cart detail to delete
     */
    @Override
    public void deleteCartDetail(CartDetail cartDetail) {
        logger.info("Deleting cart detail with id: {}", cartDetail.getId());
        cartDetailRepository.deleteById(cartDetail.getId());
        logger.debug("Cart detail deleted successfully");
    }

    /**
     * Converts a list of CartDetail entities to DTOs.
     * Each CartDetail is converted to contain product information for display.
     * @param cartDetails The list of cart details
     * @return A list of cart detail DTOs with product information
     */
    @Override
    public List<CartDetailDto> convertToCartDetailDtos(List<CartDetail> cartDetails) {
        logger.debug("Converting {} cart details to DTOs", cartDetails.size());
        List<CartDetailDto> cartDetailDtos = new ArrayList<>();

        for (CartDetail cartDetail : cartDetails) {
            CartDetailDto dto = convertCartDetailToDto(cartDetail);
            cartDetailDtos.add(dto);
        }

        logger.debug("Successfully converted all cart details to DTOs");
        return cartDetailDtos;
    }

    /**
     * Converts a single CartDetail to a DTO.
     * @param cartDetail The cart detail to convert
     * @return The converted DTO
     */
    private CartDetailDto convertCartDetailToDto(CartDetail cartDetail) {
        Product product = cartDetail.getProduct();
        CartDetailDto dto = new CartDetailDto();
        
        dto.setImage(product.getImage());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setQuantity(cartDetail.getQuantity());
        dto.setProductId(product.getId());
        dto.setCartDetailId(cartDetail.getId());
        dto.setQuantityInStock(product.getQuantity());
        
        return dto;
    }

    /**
     * Retrieves cart details by cart (legacy method for compatibility).
     * @param cart The cart
     * @return A list of cart details
     */
    public List<CartDetail> getCartDetailByCart(Cart cart) {
        return getCartDetailsByCart(cart);
    }

    /**
     * Converts cart details to DTOs (legacy method for compatibility).
     * @param cartDetails The list of cart details
     * @return A list of cart detail DTOs
     */
    public List<CartDetailDto> convertCartDetailToDto(List<CartDetail> cartDetails) {
        return convertToCartDetailDtos(cartDetails);
    }
}
