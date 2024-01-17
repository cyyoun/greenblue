package cyy.greenblue.controller;

import cyy.greenblue.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Controller
@RequiredArgsConstructor
public class LoginController {


    @ResponseBody
    @PostMapping("/auth/loginProc")
    public ResponseEntity<?> login() {
        return ResponseEntity.ok(Map.of("success", true));
    }


    @GetMapping("/auth/hello")
    public String helloForm() {
        return "hello";
    }

    @GetMapping("/user")
    @ResponseBody
    public String user(Authentication authentication) {
        return "유저 페이지입니다.";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "어드민 페이지입니다.";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {
        return "매니저 페이지입니다.";
    }

    @GetMapping("/test/login")
    @ResponseBody
    //@AuthenticationPrincipal 통해서 세션 정보에 접근할 수 있음
    public String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) {
        System.out.println("========= LoginController.testLogin =========");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); //OAuth 로그인하면 오류 발생
        System.out.println("principalDetails = " + principalDetails.getMember());

        System.out.println("userDetails = " + userDetails.getMember());
        return "세션 정보 확인하기...";
    }

    @GetMapping("/test/oauth/login")
    @ResponseBody
    public String testOauthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) {
        System.out.println("========= LoginController.testOauthLogin =========");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal(); //OAuth 로그인하면 오류 발생
        System.out.println("principalDetails = " + oAuth2User.getAttributes());

        System.out.println("oauth = " + oauth.getAttributes());
        return "OAuth 세션 정보 확인하기...";
    }
}

