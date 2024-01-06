package cyy.greenblue.service;

import cyy.greenblue.domain.*;
import cyy.greenblue.domain.status.PurchaseStatus;
import cyy.greenblue.domain.status.ReviewStatus;
import cyy.greenblue.repository.PointRepository;
import cyy.greenblue.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final OrderProductService orderProductService;

    public int currentPointByAuthentication(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
        return pointRepository.findPointByMemberId(member);
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
            throw new IllegalArgumentException("포인트 적립 불가 상태 (구매확정)");
        }
    }

    public void addReviewPoint(Review review) {
        OrderProduct orderProduct = orderProductService.findOne(review.getOrderProduct().getId());
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

    public List<Point> findAllByAuthentication(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Long memberId = principal.getMember().getId();
        return pointRepository.findByMemberId(memberId);
    }
}
