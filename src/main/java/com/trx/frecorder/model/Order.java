package com.trx.frecorder.model;

import java.util.Objects;

/**
 * @author vasilev
 */
public class Order {
    private static Long nextId = 1L;
    private final Long id;

    private OrderDetail orderDetail;

    public Order() {
        this.id = nextId++;
    }

    public Long getId() {
        return id;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
