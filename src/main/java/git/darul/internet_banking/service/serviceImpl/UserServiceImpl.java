package git.darul.internet_banking.service.serviceImpl;

import git.darul.internet_banking.dto.request.RegisterRequest;
import git.darul.internet_banking.dto.response.UserResponse;
import git.darul.internet_banking.entity.Account;
import git.darul.internet_banking.entity.User;
import git.darul.internet_banking.repository.UserRepository;
import git.darul.internet_banking.service.AccountService;
import git.darul.internet_banking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;

    @Override
    public User getOne(String id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponse create(RegisterRequest request) {
        log.info("Create user: {}", request);
        Account account = accountService.create(request.getName());
        User user = User.builder()
                .account(account)
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();
        validateRegister(user);
        String uuid = String.valueOf(UUID.randomUUID());
        userRepository.insertUser(uuid, account.getId(), user.getUsername(), user.getPassword(), user.getEmail());
        user.setId(uuid);
        return getUserResponse(user, account);
    }

    @Transactional
    @Override
    public Boolean validateRegister(User user) {
        Optional<User> allByUser = userRepository.findAllByUser(user.getEmail(), user.getUsername());
        if (!allByUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }
        return true;
    }


    private UserResponse getUserResponse(User user, Account account) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(account.getName())
                .accountNumber(account.getAccountNumber())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
