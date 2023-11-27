package cyy.greenblue.service;

import cyy.greenblue.domain.*;
import cyy.greenblue.domain.status.PurchaseStatus;
import cyy.greenblue.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final Map<Member, Integer> map = new HashMap<>();

    @Scheduled(cron = "0 0 0 1 1,4,7,10 *") // 1월, 4월, 7월, 10월의 1일 00시 00분에 실행
    public void scheduleMemberGradeUpdate() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 예약된 작업이 실행된 월의 1일 00시 00분부터 이전 월의 마지막 날 23시 59분까지의 날짜 범위 계산
        LocalDateTime fromDateTime = currentDateTime.minusMonths(3).toLocalDate().atStartOfDay();
        LocalDateTime toDateTime = currentDateTime.minusDays(1).toLocalDate().atTime(23, 59, 59);

        // 날짜와 시간 포맷 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // 계산된 날짜 범위 출력 또는 데이터 추출
        System.out.println("작업이 실행된 시간: " + currentDateTime.format(formatter));
        System.out.println("작업 범위: " + fromDateTime.format(formatter) + " ~ " + toDateTime.format(formatter));

        List<OrderSheet> orderSheets = orderSheetService.findAllByTimeRange(fromDateTime, toDateTime);
        updateMemberGrade(orderSheets);
    }

    public void updateMemberGrade(List<OrderSheet> orderSheets) {

        //회원별 누적 금액 계산
        for (OrderSheet orderSheet : orderSheets) {
            for (OrderProduct orderProduct : orderProductService.findAllByOrderSheet(orderSheet)) {
                PurchaseStatus purchaseStatus = orderProduct.getPurchaseStatus();
                if (purchaseStatus == PurchaseStatus.PURCHASE_CONFIRM ||
                        purchaseStatus == PurchaseStatus.ACCRUAL || purchaseStatus == PurchaseStatus.NON_ACCRUAL) {
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
}
