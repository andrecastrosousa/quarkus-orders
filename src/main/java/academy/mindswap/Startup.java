package academy.mindswap;

import academy.mindswap.model.User;
import academy.mindswap.repository.UserRepository;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Singleton
public class Startup {

    @Inject
    UserRepository userRepository;

    @Transactional
    public void loadAdmins() {
        User user = User.builder()
                .withName("admin")
                .withPassword("123")
                .withEmail("admin@admin.pt")
                .withRole("admin")
                .build();
        userRepository.persist(user);
    }
}
