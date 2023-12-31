package academy.mindswap.model;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "Users")
@UserDefinition
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;
    @Username
    private String email;
    @Password
    private String password;
    @Roles
    public String role;

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

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BcryptUtil.bcryptHash(password);
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static final class UserBuilder {
        private final User user;

        private UserBuilder() {
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

        public UserBuilder withRole(String role) {
            user.setRole(role);
            return this;
        }

        public User build() {
            return user;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", orders=" + orders +
                '}';
    }
}
