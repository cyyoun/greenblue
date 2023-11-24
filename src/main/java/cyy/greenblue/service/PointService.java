package cyy.greenblue.service;

import cyy.greenblue.domain.*;
import cyy.greenblue.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Service
@RequiredArgsConstructor
@EnableScheduling
public class PointService {
    private static final int AMOUNT_SUBTRACT_3HOUR = 1;
    private final PointRepository pointRepository;
    private final OrderProductService orderProductService;
    private final OrderSheetService orderSheetService;

    public int currentPoint(long memberId) {
        return pointRepository.findByMember(memberId);
    }

    @Scheduled(cron = "0 0/10 * * * ?") //10분마다 확인
    public void orderPointAdd() { //포인트 적립
        orderPointSave(1, OrderStatus.SUCCESS, OrderStatus.COMPLETE);
    }

    @Scheduled(cron = "0 0/10 * * * ?") //10분마다 확인
    public void orderPointDel() { //포인트 취소
        orderPointSave(0, OrderStatus.CANCEL, OrderStatus.CANCEL_COMPLETE);
    }

    public void orderPointSave(int multiplier, OrderStatus orderStatus, OrderStatus updateOrderStatus) {
        for (OrderSheet orderSheet : orderSheetsByRegDate(orderStatus)) {
            for (OrderProduct orderProduct : orderProductsByOrderSheet(orderSheet)) {
                
                orderProduct.getOrderSheet().updateStatus(updateOrderStatus); //주문 상태 변경
                int calcedPoint = orderPointCalc(orderProduct) * multiplier; //적립금 계산
                Member member = orderProduct.getMember();

                Point point = new Point(calcedPoint, orderProduct, member);
                pointRepository.save(point); //point 저장
            }
        }
    }

    private List<OrderProduct> orderProductsByOrderSheet(OrderSheet orderSheet) {
        return orderProductService.findAllByOrderSheet(orderSheet);
    }

    private List<OrderSheet> orderSheetsByRegDate(OrderStatus orderStatus) {
        return orderSheetService.findAllByRegDate(AMOUNT_SUBTRACT_3HOUR, orderStatus);
    }

    public int orderPointCalc(OrderProduct orderProduct) {
        double percent = (double) orderProduct.getMember().getGrade().getPercent() / 100;
        int calcPrice = orderProduct.getProduct().getPrice() * orderProduct.getQuantity();
        return (int) (percent * calcPrice); //적립률 * 가격 * 수량
    }


}
