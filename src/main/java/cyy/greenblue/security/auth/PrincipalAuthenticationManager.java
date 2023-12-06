package cyy.greenblue.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class PrincipalAuthenticationManager implements AuthenticationManager {
    private final PrincipalAuthenticationProvider principalAuthenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return principalAuthenticationProvider.authenticate(authentication);
    }
}
