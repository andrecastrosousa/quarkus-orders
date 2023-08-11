package academy.mindswap.service;

import academy.mindswap.converter.UserConverter;
import academy.mindswap.dto.UserCreateDto;
import academy.mindswap.model.User;
import academy.mindswap.repository.UserRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.matches;

//https://quarkus.io/blog/quarkus-component-test/
@QuarkusComponentTest
class UserServiceImplTest {

    @Inject
    UserServiceImpl userService;

    @InjectMock
    UserRepository userRepository;

    @InjectMock
    UserConverter userConverter;


    UserCreateDto valid_user = new UserCreateDto("test", "test@gmail.com", "Test1234", "Test1234");

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createUserCallsUserRepository() {
        @SuppressWarnings("unchecked")
        //GIVEN
        PanacheQuery<User> panacheQueryMock = Mockito.mock(PanacheQuery.class);
        UserCreateDto valid_user = new UserCreateDto("test", "test@gmail.com", "Test1234", "Test1234");

        //WHEN
        // Mock the userRepository.find() method to return a PanacheQuery mock
        Mockito.when(userRepository.find(matches("email"), anyString())).thenReturn(panacheQueryMock);

        //Mock the user converter to return a User object
        Mockito.when(userConverter.toEntityFromCreateDto(valid_user))
                .thenReturn(new User(
                        valid_user.getName(),
                        valid_user.getEmail(),
                        valid_user.getPassword()
                ));

        //Mockito.when(userRepository.persist(Mockito.any(User.class))).;
        // Call the userService.create() method
        userService.create(valid_user);

        //THEN
        // Check if userRepository.find() is called by the userService.create() method
        Mockito.verify(userRepository).find(anyString(), anyString());
        // Check if userRepository.persist() is called by the userService.create() method
        Mockito.verify(userRepository).persist(Mockito.any(User.class));
        // Check if userRepository.persist() is called only once by the userService.create() method
        Mockito.verify(userRepository, Mockito.times(1))
                .persist(Mockito.any(User.class));
    }

}