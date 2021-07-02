package com.example.AutoShop.Controller;

import com.example.AutoShop.Entity.Order;
import com.example.AutoShop.Exceptions.ResourceNotFound;
import com.example.AutoShop.Repository.CustomerRepository;
import com.example.AutoShop.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/all")
    public List<Order> get(){
        return orderRepository.findAll();
    }

    @PostMapping("/customer/{customerID}/add")
    public Order add(@PathVariable (value = "customerID") Long customerID,
                     @RequestBody Order order){
        return customerRepository.findById(customerID).map(customer -> {
            order.setCustomerID(customer);
            return orderRepository.save(order);
        }).orElseThrow(()-> new ResourceNotFound("Could not find",customerID));
    }

    @GetMapping("/customer/{customerID}/orders")
    public List<Order> getAllByCustomerID(@PathVariable (value = "customerID") Long customerID){
        return orderRepository.findAllByCustomerID(customerID);
    }

    @GetMapping("/customer/{customerID}/orders/{orderID}")
    public Order findById(@PathVariable (value = "customerID")Long customerID,
                          @PathVariable (value = "orderID") Long orderID){
        if (!customerRepository.existsById(customerID)){
            throw new ResourceNotFound("Could not find",customerID);
        }
        return orderRepository.findById(orderID)
                .orElseThrow(()-> new ResourceNotFound("Could not find",orderID));
    }

    @PutMapping("/customer/{customerID}/order/{orderID}/update")
    public Order orderUpdate(@PathVariable (value = "customerID")Long customerID,
                             @PathVariable (value = "orderID") Long orderID,
                             @RequestBody Order order){
        if (customerRepository.existsById(customerID)){
            throw new ResourceNotFound("Could not find",customerID);
        }
        return orderRepository.findById(orderID).map(order1 -> {
            return orderRepository.save(order);
        }).orElseThrow(()->new ResourceNotFound("Could not find",orderID));
    }

    @DeleteMapping("/customer/{customerID}/orders/{orderID}/delete")
    public ResponseEntity<?> orderDelete (@PathVariable (value = "customerID")Long customerID,
                                          @PathVariable (value = "orderID") Long orderID){
        return orderRepository.findByCustomerIDAndOrderID(orderID,customerID).map(order -> {
            orderRepository.delete(order);
            return ResponseEntity.ok().build();
        }).orElseThrow(()->new ResourceNotFound("Could not find",orderID));
    }
}