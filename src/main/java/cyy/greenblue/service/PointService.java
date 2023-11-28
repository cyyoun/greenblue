package cyy.greenblue.service;

import cyy.greenblue.domain.*;
import cyy.greenblue.domain.status.PurchaseStatus;
import cyy.greenblue.domain.status.ReviewStatus;
import cyy.greenblue.repository.PointRepository;
import lombok.RequiredArgsConstructor;
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

    public void addPurchaseConfirmPoint(long orderProductId) {
        OrderProduct orderProduct = orderProductService.findOne(orderProductId);
        Member member = orderProduct.getMember();
        int points = orderPointCalc(orderProduct);

        if (orderProduct.getPurchaseStatus() == PurchaseStatus.PURCHASE_CONFIRM) {
            Point point = new Point(points, orderProduct, member);
            pointRepository.save(point); //포인트 적립
            orderProductService.editPurchaseStatus(orderProduct, PurchaseStatus.ACCRUAL);
        } else {
            throw new IllegalArgumentException("포인트 적립 불가 상태 (원인 불명????????????)");
        }
    }

    public void addReviewPoint(Review review) {
        long orderProductId = review.getOrderProduct().getId();
        OrderProduct orderProduct = orderProductService.findOne(orderProductId);
        Member member = orderProduct.getMember();

        if (orderProduct.getReviewStatus() == ReviewStatus.WRITTEN) {
            Point point = new Point(review, member);
            pointRepository.save(point);
        }
    }

    public int orderPointCalc(OrderProduct orderProduct) {
        double percent = (double) orderProduct.getMember().getGrade().getPercent() / 100;
        int calcPrice = orderProduct.getProduct().getPrice() * orderProduct.getQuantity();
        return (int) (percent * calcPrice); //적립률 * 가격 * 수량
    }

    public List<Point> findAllByMemberId(long memberId) {
        return pointRepository.findByMemberId(memberId);
    }
}
