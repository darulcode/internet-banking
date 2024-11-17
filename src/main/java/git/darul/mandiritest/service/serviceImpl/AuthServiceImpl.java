package git.darul.mandiritest.service.serviceImpl;

import git.darul.mandiritest.dto.request.AuthRequest;
import git.darul.mandiritest.dto.response.AuthResponse;
import git.darul.mandiritest.entity.User;
import git.darul.mandiritest.service.AuthService;
import git.darul.mandiritest.service.JwtService;
import git.darul.mandiritest.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        User userAccount = (User) authenticate.getPrincipal();
        String accessToken = jwtService.generateToken(userAccount);
        String refreshToken = refreshTokenService.createToken(userAccount.getId());
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
