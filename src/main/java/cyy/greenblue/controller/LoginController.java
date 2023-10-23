package cyy.greenblue.controller;

import cyy.greenblue.domain.Member;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class LoginController {

    @GetMapping(value = {"/", "/home"})
    public String homeForm() {
        return "home";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
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
    @ResponseBody
    public String join(@ModelAttribute Member member) {
        return member.getEmail();
    }

    @GetMapping("/test/login")
    @ResponseBody
    public String testLogin(Authentication authentication) {
        return "세션 정보 확인하기...";
    }
}

