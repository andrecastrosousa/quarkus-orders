package academy.mindswap.service;

import academy.mindswap.converter.UserConverter;
import academy.mindswap.dto.UserCreateDto;
import academy.mindswap.dto.UserDto;
import academy.mindswap.interceptor.Logging;
import academy.mindswap.model.User;
import academy.mindswap.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import java.util.List;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    UserConverter userConverter;


    @Override

    public List<User> getAll() {
        return userRepository.findAll().list();
    }

    public User getById(Long id) {
        return userRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("User not found", 404));
    }

    @Override
    public User get(String email) {
        return userRepository.find("email", email).firstResultOptional()
                .orElseThrow(() -> new WebApplicationException("User not found", 404));
    }


    @Override
    @Logging
    public UserDto create(UserCreateDto userCreateDto) {
        if (!userCreateDto.getPassword().equals(userCreateDto.getRetypePassword())) {
            throw new WebApplicationException("Passwords do not match", 400);
        }
        if (userRepository.find("email", userCreateDto.getEmail()).firstResultOptional().isPresent()) {
            throw new WebApplicationException("Email already exists", 400);
        }

        User user = userConverter.toEntityFromCreateDto(userCreateDto);
        userRepository.persist(user);
        return userConverter.toDto(user);
    }

    @Override
    public List<UserDto> create(List<UserCreateDto> users) {
        //userRepository.persist(users);
        //return users;
        return null;
    }

    @Override
    public User update(Long id, User user) {
        User existingUser = getById(id);

        if (!user.getEmail().equals(existingUser.getEmail())) {
            throw new WebApplicationException("Email cannot be changed", 400);
        }
        existingUser.setName(user.getName());

        userRepository.persist(existingUser);
        return existingUser;
    }


    @Override
    public void delete(Long id) {
        User user = getById(id);
        userRepository.delete(user);
    }


}
