package git.darul.internet_banking.service;

import git.darul.internet_banking.dto.request.RegisterRequest;
import git.darul.internet_banking.dto.response.UserResponse;
import git.darul.internet_banking.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User getOne(String id);
    UserResponse create(RegisterRequest request);
    Boolean validateRegister(User user);
}
