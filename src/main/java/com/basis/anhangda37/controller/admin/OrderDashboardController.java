package com.basis.anhangda37.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tags.shaded.org.apache.regexp.recompile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.LobRetrievalFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import com.basis.anhangda37.controller.client.HomePageController;
import com.basis.anhangda37.domain.Order;
import com.basis.anhangda37.domain.OrderDetail;
import com.basis.anhangda37.domain.dto.OrderDetailToShowDto;
import com.basis.anhangda37.repository.OrderRepository;
import com.basis.anhangda37.service.OrderDetailService;
import com.basis.anhangda37.service.OrderService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderDashboardController {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    public OrderDashboardController(OrderService orderService, OrderDetailService orderDetailService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
    }

    @GetMapping("/admin/order")
    public String getDashboard(Model model,
            @RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Order> porders = orderService.getAllOrders(pageable);
        List<Order> orders = porders.getContent();
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", porders.getTotalPages());
        return "admin/order/show";
    }

    @GetMapping("/admin/order/{id}")
    public String getOrderDetailPage(@PathVariable("id") Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrder(order);
        List<OrderDetailToShowDto> dto = orderDetailService.convertOrderListToDtoList(orderDetails);
        model.addAttribute("orderDetails", dto);
        model.addAttribute("receiverName", order.getReceiverName());
        model.addAttribute("receiverAddress", order.getReceiverAddress());
        model.addAttribute("receiverPhone", order.getReceiverPhone());
        return "admin/order/detail";
    }

    @GetMapping("/admin/order/update/{id}")
    public String getOrderUpdatePage(@PathVariable("id") Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("updatedOrder", order);
        return "admin/order/update";
    }

    @PostMapping("/admin/order/update")
    public String postOrderUpdatePage(@ModelAttribute("updatedOrder") Order order) {
        Order fetchOrder = orderService.getOrderById(order.getId());
        orderService.updateStatus(fetchOrder, order.getStatus());
        return "redirect:/admin/order";
    }

    @GetMapping("/admin/order/delete/{id}")
    public String getDeletePage(@PathVariable("id") Long orderId, Model model) {
        model.addAttribute("id", orderId);
        Order order = new Order();
        order.setId(orderId);
        model.addAttribute("order", order);
        return "admin/order/delete";
    }

    @PostMapping("/admin/order/delete")
    public String postDeleteOrderPage(@ModelAttribute("order") Order order) {
        orderService.deleteById(order.getId());
        return "redirect:/admin/order";
    }
}
