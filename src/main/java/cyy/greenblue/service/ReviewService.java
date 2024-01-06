package cyy.greenblue.service;

import cyy.greenblue.domain.*;
import cyy.greenblue.domain.status.PurchaseStatus;
import cyy.greenblue.domain.status.ReviewStatus;
import cyy.greenblue.dto.ReviewDto;
import cyy.greenblue.dto.ReviewImgDto;
import cyy.greenblue.repository.ReviewRepository;
import cyy.greenblue.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderProductService orderProductService;
    private final ReviewImgService reviewImgService;
    private final PointService pointService;

    public ReviewDto convertDto(Review review, List<ReviewImgDto> reviewImgDtoList) {
        return new ReviewDto().toDto(review, reviewImgDtoList);
    }

    public List<ReviewDto> convertDtoList(List<Review> reviews) {
        return reviews.stream().map(review -> {
            List<ReviewImg> reviewImgList = reviewImgService.findAllByReview(review);
            ReviewDto reviewDto = new ReviewDto().toDto(review, reviewImgService.convertDtoList(reviewImgList));
            return reviewDto;
        }).toList();
    }

    private Member findMemberByAuthentication(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        return principal.getMember();
    }

    public ReviewDto add(Review review, List<MultipartFile> multipartFiles, Authentication authentication) {
            checkReviewCreate(review);
            review.updateMember(findMemberByAuthentication(authentication));
            Review savedReview = reviewRepository.save(review);
            pointService.addReviewPoint(savedReview);
            List<ReviewImgDto> reviewImgDtoList = reviewImgService.save(savedReview, multipartFiles);
            return convertDto(savedReview, reviewImgDtoList);
    }

    public void checkReviewCreate(Review review) {
        OrderProduct orderProduct = orderProductService.findOne(review.getOrderProduct().getId());
        checkPurchaseDate(orderProduct);

        if (orderProduct.getPurchaseStatus() != PurchaseStatus.PURCHASE_CONFIRM &&
                orderProduct.getPurchaseStatus() != PurchaseStatus.ACCRUAL) {
            throw new RuntimeException("구매확정을 해야합니다.");
        }
        if (orderProduct.getReviewStatus() != ReviewStatus.UNWRITTEN) {
            throw new RuntimeException("리뷰를 작성할 수 없습니다.");
        }
        orderProduct.updateReviewStatus(ReviewStatus.WRITTEN);
    }

    public void checkPurchaseDate(OrderProduct orderProduct) {
        LocalDateTime before14days = LocalDateTime.now().minus(14, ChronoUnit.DAYS);
        LocalDateTime regDate = orderProduct.getOrderSheet().getRegDate();
        if (regDate.isBefore(before14days)) {
            throw new RuntimeException("리뷰 작성 기간이 지났습니다.");
        }
    }

    public ReviewDto edit(Review review, long reviewId, List<ReviewImg> deleteImgList,
                          List<MultipartFile> multipartFiles, Authentication authentication) {
        Review oriReview = findOriReview(reviewId, authentication);
        oriReview.updateReview(review);
        List<ReviewImgDto> reviewImgDtoList = reviewImgService.edit(oriReview, deleteImgList, multipartFiles);
        return convertDto(oriReview, reviewImgDtoList);
    }

    private Review findOriReview(long reviewId, Authentication authentication) {
        List<Review> reviews = reviewRepository.findAllByMember(findMemberByAuthentication(authentication));
        return reviews.stream().filter(r -> r.getId() == reviewId).findAny().orElseThrow();
    }

    public void delete(long reviewId, Authentication authentication) {
        Review oriReview = findOriReview(reviewId, authentication);
        orderProductService.editReviewStatus(oriReview.getOrderProduct(), ReviewStatus.DELETE);
        reviewRepository.delete(oriReview);
    }

    public List<ReviewDto> findAllByProductId(long productId, Pageable pageable) {
        List<ReviewStatus> reviewStatuses = List.of(ReviewStatus.WRITTEN, ReviewStatus.ACCRUAL);
        List<OrderProduct> orderProducts =
                orderProductService.findAllByProductIdAndReviewStatus(productId, reviewStatuses);
        List<Review> reviews = reviewRepository.findAllByOrderProductList(orderProducts, pageable).toList();
        return convertDtoList(reviews);
    }

    public Pageable dynamicPageable(String sortBy, Pageable pageable) {
        if (sortBy.equals("low-score")) {
            return PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by("score").ascending());
        } else if (sortBy.equals("high-score")) {
            return PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by("score").descending());
        } else if (sortBy.equals("new")) {
            return PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by("id").descending());
        }
        return pageable;
    }
}
