package git.darul.mandiritest.service;

import git.darul.mandiritest.entity.User;

public interface JwtService {
    String generateToken(User user);
    String getUserIdFromToken(String token);
    void blacklistAccessToken(String bearerToken);
    boolean isTokenBlacklisted(String token);
}
