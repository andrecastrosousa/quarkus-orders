package academy.mindswap.dto;

public class ItemDto {
    private Long id;
    private double price;
    private String name;

    public ItemDto(Long id, double price, String name) {
        this.id = id;
        this.price = price;
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
