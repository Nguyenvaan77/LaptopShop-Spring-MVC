package com.basis.anhangda37.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.CartDetail;
import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.domain.dto.CartDetailDto;
import com.basis.anhangda37.repository.CartDetailRepository;

@Service
public class CartDetailService {
    private final CartDetailRepository cartDetailRepository;

    public CartDetailService(CartDetailRepository cartDetailRepository) {
        this.cartDetailRepository = cartDetailRepository;
    }

    public List<CartDetail> getCartDetailByCart(Cart cart) {
        return cartDetailRepository.findByCart(cart);
    }

    public List<CartDetailDto> convertCartDetailToDto(List<CartDetail> cartDetails) {
        List<CartDetailDto> cartDetailDtos = new ArrayList<>();
        for (CartDetail eachCartDetail : cartDetails) {
            Product product = eachCartDetail.getProduct();
            CartDetailDto cartDetailDto = new CartDetailDto();
            cartDetailDto.setImage(product.getImage());
            cartDetailDto.setName(product.getName());
            cartDetailDto.setPrice(product.getPrice());
            cartDetailDto.setQuantity(eachCartDetail.getQuantity());
            cartDetailDto.setProductId(product.getId());
            cartDetailDtos.add(cartDetailDto);
        }
        return cartDetailDtos;
    }
}
