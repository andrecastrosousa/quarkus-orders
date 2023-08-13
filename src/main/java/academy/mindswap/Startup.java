package academy.mindswap;

import academy.mindswap.model.User;
import academy.mindswap.repository.UserRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Singleton
public class Startup {

    @Inject
    UserRepository userRepository;

    @Transactional
    public void loadAdmins(@Observes StartupEvent event) {
        userRepository.deleteAll();
        User user = User.builder()
                .withName("admin")
                .withPassword("123")
                .withEmail("admin@admin.pt")
                .withRole("admin")
                .build();
        userRepository.persist(user);
    }
}
