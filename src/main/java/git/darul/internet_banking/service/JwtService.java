package git.darul.internet_banking.service;

import git.darul.internet_banking.entity.User;

public interface JwtService {
    String generateToken(User user);
    String getUserIdFromToken(String token);
    void blacklistAccessToken(String bearerToken);
    boolean isTokenBlacklisted(String token);
}
