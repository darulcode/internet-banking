package git.darul.mandiritest.service;

import git.darul.mandiritest.dto.request.AuthRequest;
import git.darul.mandiritest.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest authRequest);
}
