package com.basis.anhangda37.service;

import java.util.List;

import org.antlr.v4.runtime.atn.OrderedATNConfigSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.stereotype.Service;
import com.basis.anhangda37.controller.admin.OrderController;
import com.basis.anhangda37.domain.Order;
import com.basis.anhangda37.domain.OrderDetail;
import com.basis.anhangda37.domain.Product;
import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.domain.dto.OrderDetailToShowDto;
import com.basis.anhangda37.domain.dto.OrderToShowDto;
import com.basis.anhangda37.domain.enums.OrderStatus;
import com.basis.anhangda37.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).get();
    }

    public Order updateStatus(Order order, OrderStatus orderStatus) {
        order.setStatus(orderStatus);
        return order = orderRepository.save(order);
    }

    public void deleteById(Long id) {
        if(orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        }
    }

    public long countOrder() {
        return orderRepository.count();
    }


}
