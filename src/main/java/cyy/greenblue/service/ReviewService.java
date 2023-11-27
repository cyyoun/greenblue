package cyy.greenblue.service;

import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.Review;
import cyy.greenblue.domain.status.PurchaseStatus;
import cyy.greenblue.domain.status.ReviewStatus;
import cyy.greenblue.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderProductService orderProductService;

    public Review add(Review review) {
        OrderProduct orderProduct = orderProductService.findOne(review.getOrderProduct().getId());
        LocalDateTime before14days = LocalDateTime.now().minus(14, ChronoUnit.DAYS);
        LocalDateTime purchaseDate = orderProduct.getPurchaseDate();

        if (purchaseDate != null && purchaseDate.isAfter(before14days)) {
            if (orderProduct.getPurchaseStatus() != PurchaseStatus.PURCHASE_UNCONFIRM &&
                    orderProduct.getReviewStatus() == ReviewStatus.UNWRITTEN) {
                orderProduct.updateReviewStatus(ReviewStatus.WRITTEN);
                reviewRepository.save(review);
            } else {
                throw new IllegalArgumentException("리뷰를 작성할 수 없습니다.");
            }
        } else if (purchaseDate == null) {
            throw new IllegalArgumentException("구매확정을 해야 합니다.");
        } else if (purchaseDate.isBefore(before14days)) {
            throw new IllegalArgumentException("리뷰 작성 기간이 지났습니다.");
        }
        return findOne(review.getId());
    }

    public Review edit(Review review, long reviewId) {
        Review oriReview = findOne(reviewId);
        oriReview.updateReview(
                review.getScore(), review.getTitle(), review.getContent());
        return findOne(review.getId());
    }

    public Review findOne(long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow();
    }

    public void delete(long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public List<Review> findAllByProductId(long productId) {
        List<OrderProduct> orderProducts = orderProductService.findAllByProductId(productId);
        return reviewRepository.findAll()
                .stream()
                .filter(review -> orderProducts.contains(review.getOrderProduct()))
                .collect(Collectors.toList());
    }
}
