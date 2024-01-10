package cyy.greenblue.service;

import cyy.greenblue.domain.*;
import cyy.greenblue.domain.status.PurchaseStatus;
import cyy.greenblue.domain.status.ReviewStatus;
import cyy.greenblue.dto.PointDto;
import cyy.greenblue.exception.PointAccrualException;
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

    private Member findMemberByAuthentication(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        return principal.getMember();
    }

    public int currentPointByAuthentication(Authentication authentication) {
        Member member = findMemberByAuthentication(authentication);
        return pointRepository.findPointByMemberId(member);
    }

    public void addPurchaseConfirmPoint(List<Long> orderProductIdList) {
        for (Long orderProductId : orderProductIdList) {
            OrderProduct orderProduct = orderProductService.findOne(orderProductId);
            Member member = orderProduct.getMember();
            int points = orderPointCalc(orderProduct);

            if (orderProduct.getPurchaseStatus() == PurchaseStatus.PURCHASE_CONFIRM) {
                pointRepository.save(toEntity(points, orderProduct, member)); //포인트 적립
                orderProductService.editPurchaseStatus(orderProduct, PurchaseStatus.ACCRUAL);
            } else {
                throw new PointAccrualException("포인트 적립이 불가합니다.");
            }
        }
    }

    public void addReviewPoint(Review review) {
        OrderProduct orderProduct = orderProductService.findOne(review.getOrderProduct().getId());
        Member member = orderProduct.getMember();
        if (orderProduct.getReviewStatus() == ReviewStatus.WRITTEN) {
            pointRepository.save(toEntity(review, member));
        } else {
            throw new PointAccrualException("포인트 적립이 불가합니다.");
        }
    }

    public int orderPointCalc(OrderProduct orderProduct) {
        double percent = (double) orderProduct.getMember().getGrade().getPercent() / 100;
        int calcPrice = orderProduct.getProduct().getPrice() * orderProduct.getQuantity();
        return (int) (percent * calcPrice); //적립률 * 가격 * 수량
    }

    public List<PointDto> findAllByAuthentication(Authentication authentication) {
        Member member = findMemberByAuthentication(authentication);
        return pointRepository.findByMember(member).stream().map(this::convertDto).toList();
    }

    public PointDto convertDto(Point point) {
        return PointDto.builder()
                .id(point.getId())
                .points(point.getPoints())
                .reviewId(point.getReview().getId())
                .orderProductId(point.getOrderProduct().getId())
                .build();
    }

    public Point toEntity(int points, OrderProduct orderProduct, Member member) {
        return Point.builder()
                .points(points)
                .orderProduct(orderProduct)
                .member(member)
                .build();
    }

    public Point toEntity(Review review, Member member) {
        return Point.builder()
                .points(500)
                .review(review)
                .member(member)
                .build();
    }
}
