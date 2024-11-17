package git.darul.internet_banking.service.serviceImpl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import git.darul.internet_banking.entity.User;
import git.darul.internet_banking.service.JwtService;
import git.darul.internet_banking.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final RedisService redisService;

    @Value("${mandiritest.jwt-secret}")
    private String SECRET_KEY;

    @Value("${mandiritest.jwt-expiration-in-minutes}")
    private Long EXPIRATION_TIME_TOKEN;

    private final String BLACKLISTED = "BLACKLISTED";

    @Value("${mandiritest.jwt-issuer}")
    private String ISSUER;


    @Override
    public String generateToken(User userAccount) {
        log.info("Generating JWT Token :{}", userAccount.getId());
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plus(EXPIRATION_TIME_TOKEN, ChronoUnit.MINUTES))
                    .withSubject(userAccount.getId())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            log.error("error Creating JWT Token :{}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating JWT Token");
        }
    }

    @Override
    public String getUserIdFromToken(String token) {
        DecodedJWT decodedJWT = extractClaimJWT(token);
        if (decodedJWT != null) {
            return decodedJWT.getSubject();
        }
        return null;
    }


    private DecodedJWT extractClaimJWT(String token) {
        log.info("Extract Token JWT - {}", System.currentTimeMillis());
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception){
            log.error("Error while validate JWT Token: {}", exception.getMessage());
            return null;
        }
    }

    @Override
    public void blacklistAccessToken(String bearerToken) {
        String token = parseToken(bearerToken);

        DecodedJWT decodedJWT = extractClaimJWT(token);

        if (decodedJWT == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Jwt not found");
        }

        Date expiresAt = decodedJWT.getExpiresAt();
        long timeLeft = (expiresAt.getTime() - System.currentTimeMillis());

        redisService.save(token, BLACKLISTED, Duration.ofMillis(timeLeft));
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        String blacklistToken = redisService.get(token);
        return blacklistToken != null && blacklistToken.equals(BLACKLISTED);
    }

    private String parseToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
