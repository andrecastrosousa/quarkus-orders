package academy.mindswap.dto;

import academy.mindswap.model.OrderItem;
import academy.mindswap.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {
    private Long id;
    private double total;
    private LocalDateTime orderDatetime;

    private List<OrderItem> orderItems;

    public OrderDto(Long id) {
        this.id = id;
        this.total = 0;
    }

    public OrderDto(Long id, double total, LocalDateTime orderDatetime, List<OrderItem> orderItems) {
        this.id = id;
        this.total = total;
        this.orderDatetime = orderDatetime;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDateTime getOrderDatetime() {
        return orderDatetime;
    }

    public void setOrderDatetime(LocalDateTime orderDatetime) {
        this.orderDatetime = orderDatetime;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
