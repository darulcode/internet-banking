package git.darul.internet_banking.service.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;


import git.darul.internet_banking.dto.request.RegisterRequest;
import git.darul.internet_banking.dto.response.UserResponse;
import git.darul.internet_banking.entity.Account;
import git.darul.internet_banking.entity.User;
import git.darul.internet_banking.repository.UserRepository;
import git.darul.internet_banking.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Account account;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account = Account.builder().id("accountId").name("John Doe").accountNumber(12345).build();
        user = User.builder().id("userId").username("johndoe").password("encodedPassword").email("john@example.com").account(account).build();
        registerRequest = RegisterRequest.builder().username("johndoe").password("password123").email("john@example.com").name("John Doe").build();
    }

    @Test
    void testCreateUserSuccess() {
        RegisterRequest request = new RegisterRequest("johndoe", "password123", "john@example.com", "John Doe");

        Account mockAccount = new Account(
                "accountId",
                12345,
                1000L,
                "John Doe",
                new ArrayList<>()
        );

        when(accountService.create(anyString())).thenReturn(mockAccount);

        doNothing().when(userRepository).insertUser(anyString(), anyString(), anyString(), anyString(), anyString());

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        UserResponse response = userService.create(request);

        verify(userRepository).insertUser(
                anyString(),
                eq(mockAccount.getId()),
                eq(request.getUsername()),
                eq("encodedPassword"),
                eq(request.getEmail())
        );

        assertNotNull(response);
        assertEquals(request.getUsername(), response.getUsername());
        assertEquals(request.getEmail(), response.getEmail());
    }




    @Test
    void testCreateUserAlreadyExists() {
        // Mocking behavior for already existing user
        when(userRepository.findAllByUser(user.getEmail(), user.getUsername())).thenReturn(Optional.of(user));

        // Expecting ResponseStatusException
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.create(registerRequest));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("User already exists", exception.getReason());
    }

    @Test
    void testValidateRegisterUserExists() {
        // Mocking behavior for existing user
        when(userRepository.findAllByUser(user.getEmail(), user.getUsername())).thenReturn(Optional.of(user));

        // Expecting ResponseStatusException
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.validateRegister(user));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("User already exists", exception.getReason());
    }

    @Test
    void testValidateRegisterUserNotExists() {
        // Mocking behavior for non-existing user
        when(userRepository.findAllByUser(user.getEmail(), user.getUsername())).thenReturn(Optional.empty());

        // Should return true
        assertTrue(userService.validateRegister(user));
    }

    @Test
    void testLoadUserByUsernameUserFound() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.of(user));

        User loadedUser = (User) userService.loadUserByUsername(user.getUsername());

        assertNotNull(loadedUser);
        assertEquals(user.getUsername(), loadedUser.getUsername());
    }

    @Test
    void testLoadUserByUsernameUserNotFound() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.loadUserByUsername(user.getUsername()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
    }
}
