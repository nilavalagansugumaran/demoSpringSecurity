package com.example.demoSpringSecurity;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class OrdersController {

    @Autowired private OrdersRepository ordersRepository;

    @GetMapping(path = "/orders/{id}", headers = {"Content-Type=application/json", "Accept=application/json"})
    public Orders getOneOrder(@PathVariable("id") Long id){

        Optional<Orders> o = ordersRepository.findById(id);

        if(o.isPresent()) {

            return o.get();
        } else {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found for id - " + id );
        }
    }

    @GetMapping(path = "/orders", headers = {"Content-Type=application/json", "Accept=application/json"})
    public List<Orders> getAllOrders(Principal principal, Authentication auth ){

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authFromContext = securityContext.getAuthentication();
        log.debug("User from securityContext {}", authFromContext.getName());

        List<Orders> o = ordersRepository.findByCustomerName(principal.getName());

        if(!CollectionUtils.isEmpty(o)) {
            return o;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Orders not found for customer - " + auth.getName() );
        }

    }

    @PostMapping(path = "/orders", headers = {"Content-Type=application/json", "Accept=application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public Orders createOrder(@RequestBody Orders orderRequest){

       return ordersRepository.save(orderRequest);
    }

    @PutMapping(path = "/orders/{id}", headers = {"Content-Type=application/json", "Accept=application/json"})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateOrder(@PathVariable("id") Long id, @RequestBody Orders orderUpdateRequest){

        Optional<Orders> o = ordersRepository.findById(id);
        if(o.isPresent()) {
            Orders originalOrder =  o.get();
            originalOrder.setItems(orderUpdateRequest.getItems());
            originalOrder.setItemsValue(orderUpdateRequest.getItemsValue());
            ordersRepository.save(originalOrder);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found for id - " + id );
        }
    }

    @DeleteMapping(path = "/orders/{id}", headers = {"Content-Type=application/json", "Accept=application/json"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("id") Long id){

        Optional<Orders> o = ordersRepository.findById(id);
        if(o.isPresent()) {
            ordersRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found for id - " + id );
        }
    }
}
