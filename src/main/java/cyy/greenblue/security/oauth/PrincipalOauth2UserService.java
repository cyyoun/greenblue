package cyy.greenblue.security.oauth;

import cyy.greenblue.domain.Member;
import cyy.greenblue.repository.MemberRepository;
import cyy.greenblue.security.auth.PrincipalDetails;
import cyy.greenblue.security.oauth.provider.GoogleUserInfo;
import cyy.greenblue.security.oauth.provider.NaverUserInfo;
import cyy.greenblue.security.oauth.provider.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

//    private final BCryptPasswordEncoder bCryptPasswordEncoder; //순환참조 오류 발생
    private final MemberRepository memberRepository;


    // 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // 함수 종료시 @AuthenticationPrincipal 애노테이션이 만들어짐
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // getClientRegistration : 어떤 OAuth로 로그인 했는지 확인 가능
        System.out.println("getClientRegistration: "+ userRequest.getClientRegistration());
        System.out.println("getAccessToken: "+ userRequest.getAccessToken());
        System.out.println("getClientId: "+ userRequest.getClientRegistration().getClientId());


        // 구글 로그인 완료 → code 리턴(OAuth-Client 라이브러리) → AccessToken 요청하고 받은 정보가 userRequest 정보
        // userRequest 정보 → loadUser 함수 호출 → 구글로부터 회원 프로필 받음
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("getAttributes: "+ oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = null;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if (registrationId.equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (registrationId.equals("naver")) {
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        } else {
            System.out.println("간편로그인은 구글과 네이버로만 할 수 있습니다.");
        }


        //회원가입 강제 진행 코드
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + providerId;
        String password = "용리가리";
//                bCryptPasswordEncoder.encode("용리가리"); //순환참조로 오류 발생
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";


        Member memberEntity = memberRepository.findByUsername(username);
        if (memberEntity == null) {
            memberEntity = Member.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            memberRepository.save(memberEntity);
        }
        return new PrincipalDetails(memberEntity, oAuth2User.getAttributes());
        }
}
