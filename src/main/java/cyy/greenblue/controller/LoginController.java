package cyy.greenblue.controller;

import cyy.greenblue.domain.Member;
import cyy.greenblue.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping(value= {"/login", "/"})
    public String loginForm() {
        return "login";
    }

    @PostMapping("/loginProc")
    public String login() {
        return "hello";
    }

    @GetMapping("/hello")
    public String helloForm() {
        return "hello";
    }

    @GetMapping("/join")
    public String joinForm() {
        return "join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute Member member) {
        member.setRole("ROLE_USER");
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
        return "redirect:/";
    }


    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "어드민 페이지입니다.";
    }

    @Secured("ROLE_MANAGER")
    @GetMapping("/manager")
    @ResponseBody
    public String manager() {
        return "매니저 페이지입니다.";
    }

    @GetMapping("/test/login")
    @ResponseBody
    public String testLogin(Authentication authentication) {
        return "세션 정보 확인하기...";
    }
}

