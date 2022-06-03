package com.github.harboat.core.users;

import com.github.harboat.clients.exceptions.InternalServerError;
import com.github.harboat.clients.exceptions.ResourceAlreadyExists;
import com.github.harboat.clients.exceptions.ResourceNotFound;
import com.github.harboat.core.GenericResponseDto;
import com.github.harboat.core.security.roles.RoleService;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.testng.Assert.*;
import static org.mockito.BDDMockito.*;

@Listeners({MockitoTestNGListener.class})
public class UserServiceTest {

    @Mock
    private UserRepository repository;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeMethod
    public void setUp() {
        userService = new UserService(repository, roleService, passwordEncoder);
    }

    @Test
    public void createShouldCreateUserWithProperEmail() {
        //given
        given(repository.findByEmail(any())).willReturn(Optional.empty());
        UserPostDTO userPostDTO = new UserPostDTO("testMail", "testName", "password");
        given(repository.save(any())).willReturn(null);
        //when
        try {
            UserGetDTO actual = userService.create(userPostDTO);
            //then
            assertEquals(actual.getEmail(), "testMail");
        } catch (InternalServerError e) {
            fail();
        }
    }

    @Test
    public void createShouldCreateUserWithProperName() {
        //given
        given(repository.findByEmail(any())).willReturn(Optional.empty());
        UserPostDTO userPostDTO = new UserPostDTO("testMail", "testName", "password");
        given(repository.save(any())).willReturn(null);
        //when
        try {
            UserGetDTO actual = userService.create(userPostDTO);
            //then
            assertEquals(actual.getName(), "testName");
        } catch (InternalServerError e) {
            fail("InternalServerError!");
        }
    }

    @Test(expectedExceptions = ResourceAlreadyExists.class, expectedExceptionsMessageRegExp = "User already exists!")
    public void createShouldThrowWhenThereIsAlreadyUserWithThisEmail() {
        //given
        UserPostDTO userPostDTO = new UserPostDTO("testMail", "testName", "password");

        given(repository.findByEmail(any())).willReturn(Optional.of(new User()));
        //when
        try {
            userService.create(userPostDTO);
        } catch (InternalServerError e) {
            fail("InternalServerError!");
        }
    }

    @Test(expectedExceptions = ResourceNotFound.class, expectedExceptionsMessageRegExp = "User not found!")
    public void getShouldThrowWhenUserIsNotFound() {
        //given
        String email = "testMail";
        given(repository.findByEmail(email)).willReturn(Optional.empty());
        //when
        userService.get(email);
        //then
    }

    @Test
    public void getShouldReturnProperDTO() {
        //given
        String email = "testMail";
        User user = User.builder()
                .email(email)
                .name("name")
                .password("password")
                .build();
        given(repository.findByEmail(email)).willReturn(Optional.of(user));
        //when
        var actual = userService.get(email);
        //then
        assertEquals(actual, user.toDto());
    }

    @Test(expectedExceptions = ResourceNotFound.class, expectedExceptionsMessageRegExp = "User not found!")
    public void deleteShouldThrowWhenUserNotFound() {
        //given
        String email = "testEmail";
        given(repository.findByEmail(email)).willReturn(Optional.empty());
        //when
        userService.delete(email);
        //then
    }

    @Test
    public void deleteShouldReturnProperGenericResponseDto() {
        //given
        String email = "testEmail";
        User user = User.builder()
                .email(email)
                .name("name")
                .password("password")
                .build();
        given(repository.findByEmail(email)).willReturn(Optional.of(user));
        //when
        var actual = userService.delete(email);
        //then
        assertEquals(actual, new GenericResponseDto("Successfully deleted user with email: " + email));
    }
}