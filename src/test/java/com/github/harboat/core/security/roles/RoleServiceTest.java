package com.github.harboat.core.security.roles;

import com.github.harboat.clients.exceptions.ResourceAlreadyExists;
import com.github.harboat.clients.exceptions.ResourceNotFound;
import com.github.harboat.core.GenericResponseDto;
import com.github.harboat.core.security.authorities.AuthorityService;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.testng.Assert.*;
import static org.mockito.BDDMockito.*;

@Listeners({MockitoTestNGListener.class})
public class RoleServiceTest {

    @Mock
    private RoleRepository repository;
    @Mock
    private AuthorityService authorityService;
    private RoleService roleService;

    @BeforeMethod
    public void setUp() {
        roleService = new RoleService(repository, authorityService);
    }

    @Test(expectedExceptions = ResourceAlreadyExists.class)
    public void createShouldThrowWhenRoleAlreadyExists() {
        //given
        Role role = Role.builder()
                .name("role")
                .grade(1)
                .authorities(new ArrayList<>())
                .build();
        given(repository.findByName(anyString())).willReturn(Optional.of(role));
        //when
        roleService.create(role.toDto());
        //then
    }

    @Test
    public void createShouldCreateProperRole() {
        //given
        Role role = Role.builder()
                .name("role")
                .grade(1)
                .authorities(new ArrayList<>())
                .build();
        given(repository.findByName(anyString())).willReturn(Optional.empty());
        given(repository.save(any())).willReturn(null);
        //when
        var actual = roleService.create(role.toDto());
        //then
        assertEquals(actual, new GenericResponseDto("Successfully created role with name: " + role.getName()));
    }

    @Test
    public void getShouldReturnProperRoleDTO() {
        //given
        String name = "role";
        Role role = Role.builder()
                .name(name)
                .grade(1)
                .authorities(new ArrayList<>())
                .build();
        given(repository.findByName(name)).willReturn(Optional.of(role));
        //when
        RoleDTO actual = roleService.get(name);
        //then
        assertEquals(actual, role.toDto());
    }

    @Test(expectedExceptions = ResourceNotFound.class)
    public void getShouldThrowWhenThereIsNoSuchRole() {
        //given
        String name = "role";
        Role role = Role.builder()
                .name(name)
                .grade(1)
                .authorities(new ArrayList<>())
                .build();
        given(repository.findByName(name)).willReturn(Optional.empty());
        roleService.get(name);
        //then
    }
}