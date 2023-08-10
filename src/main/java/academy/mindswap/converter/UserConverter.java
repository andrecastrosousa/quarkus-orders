package academy.mindswap.converter;

import academy.mindswap.dto.UserCreateDto;
import academy.mindswap.dto.UserDto;
import academy.mindswap.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserConverter {

    @Inject
    ObjectMapper objectMapper;

    public  UserDto toDto(User user) {
        return objectMapper.convertValue(user, UserDto.class);
    }

    public  User fromDto(UserDto userDto) {
        return User.builder()
                .withId(userDto.getId())
                .withName(userDto.getName())
                .withEmail(userDto.getEmail())
                .build();
    }

    public  User toEntityFromCreateDto(UserCreateDto userCreateDto) {
        return User.builder()
                .withName(userCreateDto.getName())
                .withEmail(userCreateDto.getEmail())
                .withPassword(userCreateDto.getPassword())
                .build();
    }
}
