package academy.mindswap.model;

import jakarta.persistence.*;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static final class UserBuilder {
        private final User user;

        public UserBuilder() {
            user = new User();
        }

        public UserBuilder withId(Long id) {
            user.setId(id);
            return this;
        }

        public UserBuilder withName(String name) {
            user.setName(name);
            return this;
        }

        public UserBuilder withEmail(String email) {
            user.setEmail(email);
            return this;
        }

        public UserBuilder withPassword(String password) {
            user.setPassword(password);
            return this;
        }

        public User build() {
            return user;
        }
    }
}
