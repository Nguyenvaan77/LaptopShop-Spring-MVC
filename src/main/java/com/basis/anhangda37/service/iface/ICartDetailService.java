package com.basis.anhangda37.service.iface;

import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.dto.CartDetailDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for CartDetail-related operations.
 * Defines contracts for shopping cart item management.
 */
public interface ICartDetailService {

    /**
     * Retrieves all cart details for a specific cart.
     * @param cart The cart
     * @return A list of cart details
     */
    List<CartDetail> getCartDetailsByCart(Cart cart);

    /**
     * Retrieves a cart detail by ID.
     * @param id The cart detail ID
     * @return An Optional containing the cart detail, if found
     */
    Optional<CartDetail> getCartDetailById(Long id);

    /**
     * Deletes a cart detail.
     * @param cartDetail The cart detail to delete
     */
    void deleteCartDetail(CartDetail cartDetail);

    /**
     * Converts a list of CartDetail entities to DTOs.
     * @param cartDetails The list of cart details
     * @return A list of cart detail DTOs
     */
    List<CartDetailDto> convertToCartDetailDtos(List<CartDetail> cartDetails);
}
