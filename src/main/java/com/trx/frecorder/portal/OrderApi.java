package com.trx.frecorder.portal;

import com.trx.frecorder.model.Order;
import com.trx.frecorder.repository.InMemoryOrderRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author vasilev
 */
@RestController
@RequestMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)

public class OrderApi {

    private final InMemoryOrderRepository inMemoryOrderRepository;

    public OrderApi(InMemoryOrderRepository inMemoryOrderRepository) {
        this.inMemoryOrderRepository = inMemoryOrderRepository;
    }

    @GetMapping
    public List<Order> all() {
        return inMemoryOrderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Order one(@PathVariable Long id) {
        return inMemoryOrderRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        inMemoryOrderRepository.deleteById(id);
    }

    @PostMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Order order) {
        inMemoryOrderRepository.findById(id)
                .ifPresentOrElse(origin -> _update(origin, order),
                        () -> inMemoryOrderRepository.save(order));
    }

    @PutMapping
    public void create(@RequestBody Order order) {
        inMemoryOrderRepository.save(order);
    }

    private void _update(Order order, Order anotherOrder) {
        order.setOrderDetail(anotherOrder.getOrderDetail());

        inMemoryOrderRepository.save(order);
    }
}
