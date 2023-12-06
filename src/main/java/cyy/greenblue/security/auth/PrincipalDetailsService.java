package cyy.greenblue.security.auth;

import cyy.greenblue.domain.Member;
import cyy.greenblue.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // 시큐리티 세션(내부 Authentication(내부 UserDetails))
    // 함수 종료시 @AuthenticationPrincipal 애노테이션이 만들어짐
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }
        return new PrincipalDetails(member);
    }
}
