package academy.mindswap.dto;

import academy.mindswap.model.User;

import java.time.LocalDateTime;

public class OrderCreateDto {
    private LocalDateTime orderDatetime;

    public LocalDateTime getOrderDatetime() {
        return orderDatetime;
    }

    public void setOrderDatetime(LocalDateTime orderDatetime) {
        this.orderDatetime = orderDatetime;
    }
}
