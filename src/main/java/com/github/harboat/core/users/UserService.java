package com.github.harboat.core.users;

import com.github.harboat.clients.exceptions.InternalServerError;
import com.github.harboat.clients.exceptions.ResourceAlreadyExists;
import com.github.harboat.clients.exceptions.ResourceNotFound;
import com.github.harboat.core.GenericResponseDto;
import com.github.harboat.core.security.roles.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserGetDTO create(UserPostDTO userPostDTO) throws InternalServerError {
        if(repository.findByEmail(userPostDTO.getEmail()).isPresent()) throw new ResourceAlreadyExists("User already exists!");
        User user = User.builder()
                .email(userPostDTO.getEmail())
                .name(userPostDTO.getName())
                .role(roleService.getBasicUserRole())
                .password(passwordEncoder.encode(userPostDTO.getPassword()))
                .createdDate(LocalDateTime.now())
                .build();
        repository.save(user);
        return user.toDto();
    }

    UserGetDTO get(String email) {
        User user = repository.findByEmail(email).orElseThrow(() -> new ResourceNotFound("User not found!"));
        return user.toDto();
    }

    GenericResponseDto delete(String email) {
        User user = repository.findByEmail(email).orElseThrow(() -> new ResourceNotFound("User not found!"));
        repository.delete(user);
        return new GenericResponseDto("Successfully deleted user with email: " + email);
    }

}
