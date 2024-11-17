package git.darul.internet_banking.service;

import git.darul.internet_banking.dto.request.AuthRequest;
import git.darul.internet_banking.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest authRequest);
}
