package academy.mindswap.dto;

public class ItemDto {
    private Long id;
    private Long price;
    private String name;

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
