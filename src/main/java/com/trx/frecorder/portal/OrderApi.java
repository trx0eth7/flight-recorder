package com.trx.frecorder.portal;

import com.trx.frecorder.jfr.JfrHttpSampleTemplate;
import com.trx.frecorder.model.Order;
import com.trx.frecorder.repository.InMemoryOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final JfrHttpSampleTemplate jfrHttpSampleTemplate;


    public OrderApi(InMemoryOrderRepository inMemoryOrderRepository,
                    JfrHttpSampleTemplate jfrHttpSampleTemplate) {
        this.inMemoryOrderRepository = inMemoryOrderRepository;
        this.jfrHttpSampleTemplate = jfrHttpSampleTemplate;
    }

    @GetMapping
    public ResponseEntity<List<Order>> all() {
        return jfrHttpSampleTemplate
                .profile((req, res) -> ResponseEntity.ok(inMemoryOrderRepository.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> one(@PathVariable Long id) {
        return jfrHttpSampleTemplate
                .profile((req, res) -> ResponseEntity.ok(
                        inMemoryOrderRepository.findById(id).orElse(null)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        return jfrHttpSampleTemplate
                .profile((req, res) -> {
                    inMemoryOrderRepository.deleteById(id);
                    return new ResponseEntity<>(HttpStatus.OK);
                });
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Order order) {
        return jfrHttpSampleTemplate.profile((req, res) -> {
            inMemoryOrderRepository.findById(id)
                    .ifPresentOrElse(origin -> _update(origin, order),
                            () -> inMemoryOrderRepository.save(order));
            return new ResponseEntity<>(HttpStatus.OK);
        });
    }

    @PutMapping
    public ResponseEntity<Void> create(@RequestBody Order order) {
        return jfrHttpSampleTemplate.profile((req, res) -> {
            inMemoryOrderRepository.save(order);
            return new ResponseEntity<>(HttpStatus.OK);
        });
    }

    private void _update(Order order, Order anotherOrder) {
        order.setOrderDetail(anotherOrder.getOrderDetail());

        inMemoryOrderRepository.save(order);
    }
}
