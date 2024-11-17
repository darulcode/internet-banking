package git.darul.internet_banking.service;

public interface RefreshTokenService {
    String createToken(String userId);
    void deleteRefreshToken(String userId);
    String rotateRefreshToken(String userId);
    String getUserIdByToken(String token);
}
