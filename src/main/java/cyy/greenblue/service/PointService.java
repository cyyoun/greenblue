package cyy.greenblue.service;

import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.OrderSheet;
import cyy.greenblue.domain.OrderStatus;
import cyy.greenblue.domain.Point;
import cyy.greenblue.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
@RequiredArgsConstructor
@EnableScheduling
public class PointService {

    private final PointRepository pointRepository;
    private final OrderService orderService;

    @Scheduled(cron = "0 0/10 * * * ?") //10분마다 확인
    public void orderPointAdd() { //포인트 적립
        OrderPointProcessing(1, OrderStatus.SUCCESS, OrderStatus.COMPLETE);
    }

    private int calcedPoint(OrderProduct orderProduct) {
        return orderService.calcPoint(orderProduct); //적립률 * 가격 * 수량
    }

    public int currentPoint(long memberId) {
        return pointRepository.findByMember(memberId);
    }

    @Scheduled(cron = "0 0/10 * * * ?") //10분마다 확인
    public void orderPointDel() { //포인트 취소
        OrderPointProcessing(0, OrderStatus.CANCEL, OrderStatus.CANCEL_COMPLETE);
    }

    private void OrderPointProcessing(int multiplier, OrderStatus orderStatus, OrderStatus updateOrderStatus) {
        for (OrderSheet orderSheet : orderService.findAllByRegDate(1, orderStatus)) {
            for (OrderProduct orderProduct : orderService.findAllByOrderSheet(orderSheet)) {
                orderProduct.getOrderSheet().updateStatus(updateOrderStatus); //주문 상태 complete 으로 변경

                int calced = calcedPoint(orderProduct) * multiplier;
                Point point = new Point(calced, orderProduct, orderProduct.getMember()); //point 취소
                pointRepository.save(point); //point 저장
            }
        }
    }

}
