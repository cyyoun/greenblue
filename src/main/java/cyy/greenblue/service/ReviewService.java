package cyy.greenblue.service;

import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.Review;
import cyy.greenblue.domain.status.PurchaseStatus;
import cyy.greenblue.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderProductService orderProductService;

    public Review add(Review review, long orderProductId) {
        //구매확정 확인
        OrderProduct orderProduct = orderProductService.findOne(orderProductId);
        PurchaseStatus purchaseStatus = orderProduct.getPurchaseStatus();
        if (findByOrderProduct(orderProduct) != null) {
            throw new IllegalStateException("이 주문 제품에 대한 리뷰가 이미 작성되었습니다.");
        }

        if (purchaseStatus.equals(PurchaseStatus.SUCCESS)) {
            return reviewRepository.save(review);
        } else {
            throw new IllegalStateException("구매 확정되지 않아, 리뷰를 추가할 수 없습니다.");
        }
    }

    public Review findByOrderProduct(OrderProduct orderProduct) {
        return reviewRepository.findByOrderProduct(orderProduct);
    }

    public Review findOne(long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow();
    }
}
