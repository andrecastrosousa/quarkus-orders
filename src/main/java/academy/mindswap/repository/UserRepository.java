package academy.mindswap.repository;


import academy.mindswap.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
 /*   List<User> users = new ArrayList<>();



    public List<User> getAll() {
        return users;
    }


    public User get(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new WebApplicationException("User not found", 404));
    }



    public User create(User user) {
        users.add(user);
        return user;
    }


    public List<User> create(List<User> users) {
        this.users.addAll(users);
        return users;
    }


    public User update(User old, User newUser) {
        int existingUserIndex = users.indexOf(old);
        users.set(existingUserIndex, newUser);
        return newUser;
    }



    public void delete(String email) {
        users.removeIf(user -> user.getEmail().equals(email));
    }*/

}
