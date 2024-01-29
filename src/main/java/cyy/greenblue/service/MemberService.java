package cyy.greenblue.service;

import cyy.greenblue.domain.*;
import cyy.greenblue.domain.status.PurchaseStatus;
import cyy.greenblue.dto.MemberDto;
import cyy.greenblue.dto.SimpleMemberInfoDto;
import cyy.greenblue.exception.MemberInfoDuplicateException;
import cyy.greenblue.repository.MemberRepository;
import cyy.greenblue.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final OrderProductService orderProductService;
    private final OrderSheetService orderSheetService;
    private final PasswordEncoder passwordEncoder;
    private static final Map<Member, Integer> map = new HashMap<>();

    @Scheduled(cron = "0 0 0 1 1,4,7,10 *") // 1월, 4월, 7월, 10월의 1일 00시 00분에 실행
    public void scheduleMemberGradeUpdate() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 예약된 작업이 실행된 월의 1일 00시 00분부터 이전 월의 마지막 날 23시 59분까지의 날짜 범위 계산
        LocalDateTime fromDateTime =
                currentDateTime.minusMonths(3).toLocalDate().atStartOfDay();
        LocalDateTime toDateTime =
                currentDateTime.minusDays(1).toLocalDate().atTime(23, 59, 59);

        List<OrderSheet> orderSheets = orderSheetService.findAllByTimeRange(fromDateTime, toDateTime);
        updateMemberGrade(orderSheets);
    }

    public void updateMemberGrade(List<OrderSheet> orderSheets) {

        //회원별 누적 금액 계산
        for (OrderSheet orderSheet : orderSheets) {
            for (OrderProduct orderProduct : orderProductService.findAllByOrderSheet(orderSheet)) {
                PurchaseStatus purchaseStatus = orderProduct.getPurchaseStatus();
                if (purchaseStatus == PurchaseStatus.PURCHASE_CONFIRM ||
                        purchaseStatus == PurchaseStatus.ACCRUAL ||
                        purchaseStatus == PurchaseStatus.NON_ACCRUAL) {

                Member member = orderProduct.getMember();
                int price = orderProduct.getProduct().getPrice() * orderProduct.getQuantity();
                map.put(member, map.getOrDefault(member, 0) + price);
                }
            }
        }
        grading();
    }


    public void grading() {
        //회원 등급 업데이트

        for (Member member : members()) {
            Grade grade = Grade.BRONZE;
            if (map.containsKey(member)) {
                int sumOrderPrice = map.get(member);
                int price = sumOrderPrice / 10000;

                if (price < 30) { //0 ~ 29 만원
                } else if (price < 50) {
                    grade = Grade.SILVER; //30 ~ 49 만원
                } else if (price < 100) {
                    grade = Grade.GOLD; //50 ~ 99 만원
                } else if (price < 150) {
                    grade = Grade.PLATINUM; //100 ~ 149 만원
                } else {
                    grade = Grade.DIAMOND; //150 만원 이상
                }
            }
            member.updateGrade(grade);
        }
    }

    public List<Member> members () {
        return memberRepository.findAll();
    }

    public void save(MemberDto memberDto) {
        System.out.println("memberDto = " + memberDto.getUsername());
        if (isUsernameDuplicate(memberDto.getUsername())) {
            throw new MemberInfoDuplicateException("아이디가 이미 사용중입니다.");
        }
        if (isEmailDuplicate(memberDto.getEmail())) {
            throw new MemberInfoDuplicateException("이메일이 이미 사용중입니다.");
        }
        Member member = convertEntity(memberDto);
        memberRepository.save(member);
    }

    public Member convertEntity(MemberDto memberDto) {
        return Member.builder()
                .username(memberDto.getUsername())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .email(memberDto.getEmail())
                .regDate(LocalDateTime.now())
                .grade(Grade.BRONZE)
                .role("ROLE_USER")
                .build();
    }

    public boolean isUsernameDuplicate(String username) {
        return memberRepository.countByUsername(username) > 0;
    }

    public boolean isEmailDuplicate(String email) {
        return memberRepository.countByEmail(email) > 0;
    }

    public SimpleMemberInfoDto findSimpleMemberInfo(Authentication authentication) {
        Member member = findMemberByAuthentication(authentication);
        return toSimpleMemberDto(member);
    }

    private Member findMemberByAuthentication(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        return principal.getMember();
    }

    public SimpleMemberInfoDto toSimpleMemberDto(Member member) {
        return SimpleMemberInfoDto.builder()
                .username(member.getUsername())
                .grade(member.getGrade())
                .build();
    }
}
