package com.basis.anhangda37.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.basis.anhangda37.domain.Order;
import com.basis.anhangda37.domain.OrderDetail;
import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.domain.dto.OrderDetailToShowDto;
import com.basis.anhangda37.repository.OrderDetailRepository;

@Service
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    public List<OrderDetail> getOrderDetailsByOrder(Order order) {
        return orderDetailRepository.findByOrder(order);
    }

    public OrderDetailToShowDto convertOrderToOrderDto(OrderDetail orderDetail) {
        Product product = orderDetail.getProduct();
        OrderDetailToShowDto dto = new OrderDetailToShowDto();
        dto.setImage(product.getImage());
        dto.setName(product.getName());
        dto.setQuantity(orderDetail.getQuantity());
        dto.setPrice(orderDetail.getPrice());
        return dto;
    }

    public List<OrderDetailToShowDto> convertOrderListToDtoList(List<OrderDetail> orderDetails) {
        List<OrderDetailToShowDto> dtos = new ArrayList<>();
        orderDetails.forEach(e -> {
            dtos.add(convertOrderToOrderDto(e));
        });
        return dtos;
    }
}
