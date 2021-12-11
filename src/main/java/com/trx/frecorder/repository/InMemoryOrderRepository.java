package com.trx.frecorder.repository;

import com.trx.frecorder.model.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author vasilev
 */
@Component
public class InMemoryOrderRepository {
    private final List<Order> orders = Collections.synchronizedList(new ArrayList<>());

    public void save(Order order) {
        var idx = orders.indexOf(order);

        if (idx < 0) {
            orders.add(order);
        } else {
            orders.set(idx, order);
        }
    }

    public Optional<Order> findById(Long id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findAny();
    }

    public List<Order> findAll() {
        return orders;
    }

    public long count() {
        return orders.size();
    }

    public void deleteById(Long id) {
        orders.removeIf(order -> order.getId().equals(id));
    }

    public void delete(Order order) {
        orders.removeIf(origin -> origin.equals(order));
    }

    public void deleteAll() {
        orders.clear();
    }
}
