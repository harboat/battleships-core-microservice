package com.github.harboat.core.security.authorities;

import com.github.harboat.clients.exceptions.ResourceAlreadyExists;
import com.github.harboat.clients.exceptions.ResourceNotFound;
import com.github.harboat.core.GenericResponseDto;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.testng.Assert.*;
import static org.mockito.BDDMockito.*;

@Listeners({MockitoTestNGListener.class})
public class AuthorityServiceTest {

    @Mock
    private AuthorityRepository repository;
    private AuthorityService service;
    private String name;

    @BeforeMethod
    public void setUp() {
        service = new AuthorityService(repository);
        name = "testAuthority";
    }

    @Test(expectedExceptions = ResourceAlreadyExists.class)
    public void createShouldThrowWhenAuthorityAlreadyExists () {
        //given
        Authority authority = Authority.builder()
                .name(name)
                .build();
        given(repository.findByName(name)).willReturn(Optional.of(authority));
        AuthorityDTO authorityDTO = new AuthorityDTO(name);
        //when
        service.create(authorityDTO);
        //then
    }

    @Test
    public void createShouldReturnProperGenericResponseDto() {
        //given
        given(repository.save(any())).willReturn(null);
        given(repository.findByName(name)).willReturn(Optional.empty());
        AuthorityDTO authorityDTO = new AuthorityDTO(name);
        //when
        var actual = service.create(authorityDTO);
        //then
        assertEquals(actual, new GenericResponseDto("Successfully created authority with name: " + name));
    }

    @Test(expectedExceptions = ResourceNotFound.class)
    public void getShouldThrowWhenThereIsNoSuchAuthority() {
        //given
        given(repository.findByName(name)).willReturn(Optional.empty());
        //when
        service.get(name);
        //then
    }

    @Test
    public void getShouldReturnProperDTO() {
        //given
        Authority authority = Authority.builder()
                .name(name)
                .build();
        given(repository.findByName(name)).willReturn(Optional.of(authority));
        //when
        var actual = service.get(name);
        //then
        assertEquals(actual, authority.toDto());
    }

    @Test(expectedExceptions = ResourceNotFound.class)
    public void deleteShouldThrowWhenThereIsNoSuchAuthority() {
        //given
        given(repository.findByName(name)).willReturn(Optional.empty());
        //when
        service.delete(name);
        //then
    }

    @Test
    public void deleteShouldReturnProperDTO() {
        //given
        Authority authority = Authority.builder()
                .name(name)
                .build();
        given(repository.findByName(name)).willReturn(Optional.of(authority));
        //when
        var actual = service.delete(name);
        //then
        assertEquals(actual, new GenericResponseDto("Successfully deleted authority with name: " + name));
    }

}