package com.github.harboat.core.security.authorities;

import com.github.harboat.clients.exceptions.InternalServerError;
import com.github.harboat.clients.exceptions.ResourceAlreadyExists;
import com.github.harboat.clients.exceptions.ResourceNotFound;
import com.github.harboat.core.GenericCRUDService;
import com.github.harboat.core.GenericResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class AuthorityService implements GenericCRUDService<AuthorityDTO, AuthorityDTO, AuthorityDTO, GenericResponseDto> {

    private final AuthorityRepository repository;

    @Override
    public GenericResponseDto create(AuthorityDTO authorityDTO) {
        if (repository.findByName(authorityDTO.getName()).isPresent()) throw new ResourceAlreadyExists();
        Authority authority = Authority.builder()
                .name(authorityDTO.getName())
                .build();
        repository.save(authority);
        return new GenericResponseDto("Successfully created authority with name: " + authority.getName());
    }

    @Override
    public AuthorityDTO get(String name) {
        Authority authority = repository.findByName(name).orElseThrow(ResourceNotFound::new);
        return authority.toDto();
    }

    @Override
    public Collection<AuthorityDTO> getAll() {
        return repository.findAll().stream()
                .map(Authority::toDto)
                .toList();
    }

    @Override
    public AuthorityDTO update(String name, AuthorityDTO authorityDTO) throws InternalServerError {
        throw new InternalServerError("Error");

    }

    @Override
    public GenericResponseDto delete(String name) {
        Authority authority = repository.findByName(name).orElseThrow(ResourceNotFound::new);
        repository.delete(authority);
        return new GenericResponseDto("Successfully deleted authority with name: " + authority.getName());
    }

    public Authority getAuthority(String name) {
        return repository.findByName(name)
                .orElseThrow(ResourceNotFound::new);
    }

    public Collection<Authority> getAuthorities(Collection<String> names) {
        return names.stream()
                .map(a -> repository.findByName(a).orElseThrow(ResourceNotFound::new))
                .toList();
    }

}
