package git.darul.mandiritest.util;

import git.darul.mandiritest.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class AuthenticationContextUtil {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("current user: {}", authentication.getPrincipal());
        if (authentication.getPrincipal()== "anonymousUser") return null;
        return (User) authentication.getPrincipal();
    }
}