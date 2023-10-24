package cyy.greenblue.security.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    // 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // getClientRegistration : 어떤 OAuth로 로그인 했는지 확인 가능
        System.out.println("getClientRegistration: "+ userRequest.getClientRegistration());
        System.out.println("getAccessToken: "+ userRequest.getAccessToken());
        System.out.println("getClientId: "+ userRequest.getClientRegistration().getClientId());
        System.out.println("getAttributes: "+ super.loadUser(userRequest).getAttributes());

        // 구글 로그인 완료 → code 리턴(OAuth-Client 라이브러리) → AccessToken 요청하고 받은 정보가 userRequest 정보
        // userRequest 정보 → loadUser 함수 호출 → 구글로부터 회원 프로필 받음
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return super.loadUser(userRequest);
    }
}
