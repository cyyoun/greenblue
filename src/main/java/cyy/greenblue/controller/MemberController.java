package cyy.greenblue.controller;

import cyy.greenblue.dto.MemberDto;
import cyy.greenblue.dto.SimpleMemberInfoDto;
import cyy.greenblue.service.MemberService;
import cyy.greenblue.util.ValidationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody MemberDto memberDto, BindingResult bindingResult) {
        ValidationUtil.chkBindingResult(bindingResult);
        memberService.save(memberDto);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @GetMapping("/member")
    public SimpleMemberInfoDto getUsernameAndGrade(Authentication authentication) {
        return memberService.findSimpleMemberInfo(authentication);
    }
}
