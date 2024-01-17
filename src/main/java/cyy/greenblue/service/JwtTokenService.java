package cyy.greenblue.service;

import cyy.greenblue.domain.JwtToken;
import cyy.greenblue.domain.Member;
import cyy.greenblue.repository.JwtTokenRepository;
import cyy.greenblue.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JwtTokenService {
    private final JwtTokenRepository jwtTokenRepository;


    public Member findMemberByAuthentication(Authentication authentication) {
        PrincipalDetails details = (PrincipalDetails) authentication.getPrincipal();
        return details.getMember();
    }

    public JwtToken save(Authentication authentication, String token) {
        JwtToken jwtToken = findByAuthentication(authentication);
        Member member = findMemberByAuthentication(authentication);
        JwtToken newToken = null;
        if (jwtToken == null) {
            newToken = jwtTokenRepository.save(new JwtToken(member, token));
        } else {
            newToken = jwtToken.updateToken(token);
        }
        jwtTokenRepository.flush();
        return newToken;
    }

    public JwtToken findByAuthentication(Authentication authentication) {
        Member member = findMemberByAuthentication(authentication);
        return jwtTokenRepository.findByMember(member).orElse(null);
    }
}
