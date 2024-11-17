package git.darul.internet_banking.controller;


import git.darul.internet_banking.constants.Constants;
import git.darul.internet_banking.dto.request.AuthRequest;
import git.darul.internet_banking.dto.request.RegisterRequest;
import git.darul.internet_banking.dto.response.AuthResponse;
import git.darul.internet_banking.dto.response.UserResponse;
import git.darul.internet_banking.service.AuthService;
import git.darul.internet_banking.service.UserService;
import git.darul.internet_banking.util.ResponseUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@RestController
@RequestMapping(Constants.AUTH_API)
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletResponse response){
        AuthResponse login = authService.login(request);
        setCookie(response, login.getRefreshToken());
        return ResponseUtil.buildResponse(HttpStatus.OK, "Successfully Authenticated",login);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        log.info("Request received: {}", request);
        UserResponse response = userService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Successfully Registered",response);
    }


    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie cookie = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(Constants.REFRESH_TOKEN_COOKIE_NAME))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, Constants.REFRESH_TOKEN_REQUIRED_MESSAGE));
        return cookie.getValue();
    }

    private void setCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(Constants.REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);
    }

}
