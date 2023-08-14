package academy.mindswap;

import academy.mindswap.model.User;
import academy.mindswap.repository.UserRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

@Singleton
public class Startup {

    @Inject
    UserRepository userRepository;

    @Transactional
    public void loadAdmins(@Observes StartupEvent event) {
        userRepository.deleteAll();
        User admin = User.builder()
                .withName("admin")
                .withPassword("123")
                .withEmail("admin@admin.pt")
                .withRole("admin")
                .build();

        User user = User.builder()
                .withName("andré")
                .withPassword("ola123")
                .withEmail("andré@gmail.com")
                .withRole("user")
                .build();

        userRepository.persist(admin);
        userRepository.persist(user);


    }
}
