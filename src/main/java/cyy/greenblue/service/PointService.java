package cyy.greenblue.service;

import cyy.greenblue.domain.*;
import cyy.greenblue.domain.status.PointStatus;
import cyy.greenblue.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final OrderProductService orderProductService;

    public int currentPoint(long memberId) {
        return pointRepository.findByMember(memberId);
    }

    @Scheduled(cron = "0 0/10 * * * ?") //10분마다 확인
    public void orderPointAdd() { //포인트 적립 (ORDER_SUCCESS → SUCCESS 로 변경)
        List<OrderProduct> orderProducts = getAllByPointStatus(PointStatus.ORDER_SUCCESS);
        save(orderProducts, PointStatus.SUCCESS);
    }

    @Scheduled(cron = "0 0/10 * * * ?") //10분마다 확인
    public void orderPointDel() { //포인트 취소 (ORDER_CANCEL → CANCEL 로 변경)
        List<OrderProduct> orderProducts = getAllByPointStatus(PointStatus.ORDER_CANCEL);
        update(orderProducts, PointStatus.CANCEL);
    }

    private List<OrderProduct> getAllByPointStatus(PointStatus pointStatus) {
        return orderProductService.findAllByPointStatus(pointStatus);
    }

    public void save(List<OrderProduct> orderProducts, PointStatus pointStatus) {
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.updatePointStatus(pointStatus); //적립 상태 변경
            int calcedPoint = orderPointCalc(orderProduct) ; //적립금 계산
            Member member = orderProduct.getMember();

            Point point = new Point(calcedPoint, orderProduct, member);
            pointRepository.save(point); //point 저장
        }
    }

    public void update(List<OrderProduct> orderProducts, PointStatus pointStatus) {
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.updatePointStatus(pointStatus); //적립 상태 변경
            findByOrderProduct(orderProduct).updateSavedPoint(0); //적립금 취소
        }
    }

    public int orderPointCalc(OrderProduct orderProduct) {
        double percent = (double) orderProduct.getMember().getGrade().getPercent() / 100;
        int calcPrice = orderProduct.getProduct().getPrice() * orderProduct.getQuantity();
        return (int) (percent * calcPrice); //적립률 * 가격 * 수량
    }

    public Point findByOrderProduct(OrderProduct orderProduct) {
        return pointRepository.findByOrderProduct(orderProduct);
    }
}
