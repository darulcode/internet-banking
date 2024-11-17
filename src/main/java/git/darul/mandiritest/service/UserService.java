package git.darul.mandiritest.service;

import git.darul.mandiritest.dto.request.RegisterRequest;
import git.darul.mandiritest.dto.response.UserResponse;
import git.darul.mandiritest.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User getOne(String id);
    UserResponse create(RegisterRequest request);
    Boolean validateRegister(User user);
}
