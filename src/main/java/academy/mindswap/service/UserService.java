package academy.mindswap.service;

import academy.mindswap.dto.UserCreateDto;
import academy.mindswap.dto.UserDto;
import academy.mindswap.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User get(String email);

    User getById(Long id);

    UserDto create(UserCreateDto userCreateDto);

    List<UserDto> create(List<UserCreateDto> users);

    User update(Long id, User user);

    void delete(Long id);

}
