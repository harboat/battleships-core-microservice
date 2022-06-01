package com.github.harboat.core.security.roles;

import com.github.harboat.clients.exceptions.InternalServerError;
import com.github.harboat.clients.exceptions.ResourceAlreadyExists;
import com.github.harboat.clients.exceptions.ResourceNotFound;
import com.github.harboat.core.GenericCRUDService;
import com.github.harboat.core.GenericResponseDto;
import com.github.harboat.core.security.authorities.AuthorityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class RoleService implements GenericCRUDService<RoleDTO, RoleDTO, RoleDTO, GenericResponseDto> {

    private final RoleRepository repository;
    private final AuthorityService authorityService;

    @Override
    public GenericResponseDto create(RoleDTO roleDTO) {
        if (repository.findByName(roleDTO.getName()).isPresent()) throw new ResourceAlreadyExists();
        Role role = Role.builder()
                .name(roleDTO.getName())
                .grade(roleDTO.getGrade())
                .authorities(authorityService.getAuthorities(roleDTO.getAuthorities()))
                .build();
        repository.save(role);
        return new GenericResponseDto("Successfully created role with name: " + role.getName());
    }


    @Override
    public RoleDTO get(String name) {
        Role role = repository.findByName(name).orElseThrow(ResourceNotFound::new);
        return role.toDto();

    }

    @Override
    public Collection<RoleDTO> getAll() {
        return repository.findAll().stream()
                .map(Role::toDto)
                .toList();
    }

    @Override
    public RoleDTO update(String name, RoleDTO roleDTO) throws InternalServerError {
        throw new InternalServerError("Error");
    }

    @Override
    public GenericResponseDto delete(String name) {
        Role role = repository.findByName(name).orElseThrow(ResourceNotFound::new);
        repository.delete(role);
        return new GenericResponseDto("Successfully deleted role with name: " + role.getName());
    }


    public Role getBasicUserRole() throws InternalServerError {
        return repository.findByAuthoritiesContains(
                        authorityService.getAuthority("BASIC_USER")
                )
                .orElseThrow(() -> new InternalServerError("Could not find role for user"));
    }
}
