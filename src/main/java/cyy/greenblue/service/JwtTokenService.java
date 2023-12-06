package cyy.greenblue.service;

import cyy.greenblue.domain.JwtToken;
import cyy.greenblue.domain.Member;
import cyy.greenblue.repository.JwtTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JwtTokenService {
    private final JwtTokenRepository jwtTokenRepository;


    public JwtToken save(JwtToken jwtToken) {
        return jwtTokenRepository.save(jwtToken);
    }

    public String findTokenByMember(Member member) {
        return findByMember(member).getToken();
    }

    public JwtToken editToken(JwtToken jwtToken) {
        JwtToken token = findByMember(jwtToken.getMember());
        token.updateToken(jwtToken.getToken());
        return token;
    }

    public JwtToken findByMember(Member member) {
        return jwtTokenRepository.findByMember(member);
    }

}
