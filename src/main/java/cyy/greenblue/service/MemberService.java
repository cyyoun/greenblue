package cyy.greenblue.service;

import cyy.greenblue.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
@EnableScheduling
public class MemberService {
    private static final int AMOUNT_SUBTRACT_3MONTH = 2190;
    private final OrderProductService orderProductService;
    private final OrderSheetService orderSheetService;
    Map<Member, Integer> map = new HashMap<>();

    @Scheduled(cron = "0 0 0 1 3/3 *") // 3개월마다 실행되도록 스케줄링
    public void scheduleMemberGradeUpdate() {
        updateMemberGrade();
    }

    public void updateMemberGrade() {
        List<OrderSheet> allOrderSheets = new ArrayList<>();
        allOrderSheets.addAll(orderSheets(OrderStatus.COMPLETE));
        allOrderSheets.addAll(orderSheets(OrderStatus.SUCCESS));

        for (OrderSheet orderSheet : allOrderSheets) {
            for (OrderProduct orderProduct : orderProductService.findAllByOrderSheet(orderSheet)) {
                Member member = orderProduct.getMember();
                int price = orderProduct.getProduct().getPrice() * orderProduct.getQuantity();
                map.put(member, map.getOrDefault(member, 0) + price);
            }
        }
        for (Member member : map.keySet()) {
            Integer sumOrderPrice = map.get(member);
            member.updateGrade(grading(sumOrderPrice));
        }
    }

    public Grade grading(int sumOrderPrice) {
        int price = sumOrderPrice / 10000;

        if (price < 30) {
            return Grade.BRONZE; //0 ~ 29
        } else if (price < 50) {
            return Grade.SILVER; //30 ~ 49
        } else if (price < 100) {
            return Grade.GOLD; //50 ~ 99
        } else if (price < 150) {
            return Grade.PLATINUM; //100 ~ 149
        } else {
            return Grade.DIAMOND; //150 이상
        }
    }

    public List<OrderSheet> orderSheets(OrderStatus orderStatus) {
        return orderSheetService.findAllByRegDate(AMOUNT_SUBTRACT_3MONTH, orderStatus);
    }
}
