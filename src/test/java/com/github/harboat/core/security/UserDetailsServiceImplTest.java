package com.github.harboat.core.security;

import com.github.harboat.clients.exceptions.BadRequest;
import com.github.harboat.core.users.UserRepository;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.BDDMockito.*;

@Listeners(MockitoTestNGListener.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;
    private UserDetailsServiceImpl service;
    private String email;

    @BeforeMethod
    public void setUp() {
        service = new UserDetailsServiceImpl(userRepository);
        email = "test@example.com";
    }

    @DataProvider
    public static Object[][] invalidEmails() {
        return new Object[][]{
                {"111"}
                , {"dfghjk"}
        };
    }

    @Test(dataProvider = "invalidEmails", expectedExceptions = BadRequest.class, expectedExceptionsMessageRegExp = "Provided email is not valid!")
    public void loadUserByUsernameShouldThrowWhenEmailIsNotValid(String email) {
        //given
        //when
        service.loadUserByUsername(email);
        //then
    }

    @Test(expectedExceptions = BadRequest.class, expectedExceptionsMessageRegExp = "User not found or credentials are not valid!")
    public void loadUserByUsernameShouldThrowWhenThereIsNoUserWithSuchEmail() {
        //given
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
        //when
        service.loadUserByUsername(email);
        //then
    }

}