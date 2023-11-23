package cyy.greenblue.service;

import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.OrderSheet;
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

    final String COMPLETE = "complete";

    private final PointRepository pointRepository;
    private final OrderService orderService;

    @Scheduled(cron = "0 0/1 * * * ?") //1분마다 확인
    public void orderPointAdd() {
        //주문 상태가 success 이면서 1시간이내 주문 건 목록 가져오기
        for (OrderSheet orderSheet : orderService.findAllByRegDate(1)) {
            for (OrderProduct orderProduct : orderService.findAllByOrderSheet(orderSheet)) {
                int calcedPoint = orderService.calcPoint(orderProduct); //적립률 * 가격 * 수량
                orderProduct.getOrderSheet().updateStatus(COMPLETE); //주문 상태 complete 으로 변경
                Point point = new Point(calcedPoint, orderProduct, orderProduct.getMember()); //point 생성
                pointRepository.save(point); //point 저장
            }
        }
    }
}
