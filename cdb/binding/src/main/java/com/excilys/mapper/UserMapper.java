package com.excilys.mapper;

import java.util.Optional;

import org.springframework.lang.Nullable;

import com.excilys.model.User;
import com.excilys.model.UserDto;

public class UserMapper {
    public static Optional<User> fromDto(@Nullable final UserDto dto) {
        if (dto == null) {
            return Optional.empty();
        }
        final User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setId(dto.getId());
        user.setRole(dto.getRole());
        return Optional.of(user);
    }

    public static Optional<UserDto> toDto(@Nullable final User user) {
        if (user == null) {
            return Optional.empty();
        }
        final UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setId(user.getId());
        dto.setRole(user.getRole());
        return Optional.of(dto);
    }
}
