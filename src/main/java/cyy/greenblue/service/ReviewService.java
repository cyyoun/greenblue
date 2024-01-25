package cyy.greenblue.service;

import cyy.greenblue.domain.*;
import cyy.greenblue.domain.status.PurchaseStatus;
import cyy.greenblue.domain.status.ReviewStatus;
import cyy.greenblue.dto.ReviewInputDto;
import cyy.greenblue.dto.ReviewOutputDto;
import cyy.greenblue.dto.ReviewImgDto;
import cyy.greenblue.exception.PurchaseConfirmException;
import cyy.greenblue.exception.ReviewException;
import cyy.greenblue.repository.ReviewRepository;
import cyy.greenblue.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderProductService orderProductService;
    private final ReviewImgService reviewImgService;



    private Member findMemberByAuthentication(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        return principal.getMember();
    }

    public ReviewOutputDto add(ReviewInputDto reviewInputDto, List<MultipartFile> multipartFiles, Authentication authentication) {
        OrderProduct orderProduct = orderProductService.findOne(reviewInputDto.getOrderProductId());
        chkPurchaseDate(orderProduct);
        chkReviewAndPurchaseStatus(orderProduct);
        orderProduct.updateReviewStatus(ReviewStatus.WRITTEN);

        Review review = toEntity(reviewInputDto, orderProduct, findMemberByAuthentication(authentication));
        Review savedReview = reviewRepository.save(review);
        List<ReviewImgDto> reviewImgDtoList = reviewImgService.save(savedReview, multipartFiles);
        return convertDto(savedReview, reviewImgDtoList);
    }

    public void chkReviewAndPurchaseStatus(OrderProduct orderProduct) {
        if (orderProduct.getPurchaseStatus() != PurchaseStatus.PURCHASE_CONFIRM &&
                orderProduct.getPurchaseStatus() != PurchaseStatus.ACCRUAL) {
            throw new PurchaseConfirmException("구매확정을 해야합니다.");
        }

        if (EnumSet.of(ReviewStatus.WRITTEN, ReviewStatus.ACCRUAL, ReviewStatus.DELETE)
                .contains(orderProduct.getReviewStatus())) {
            throw new ReviewException("이미 작성된 리뷰입니다.");
        }
    }

    public void chkPurchaseDate(OrderProduct orderProduct) {
        LocalDateTime before14days = LocalDateTime.now().minus(14, ChronoUnit.DAYS);
        LocalDateTime regDate = orderProduct.getOrderSheet().getRegDate();
        if (regDate.isBefore(before14days)) {
            throw new ReviewException("리뷰 작성 기간이 지났습니다.");
        }
    }

    public ReviewOutputDto edit(ReviewInputDto reviewInputDto, long reviewId, List<Long> deleteImgList,
                                List<MultipartFile> multipartFiles, Authentication authentication) {
        Review oriReview = findByIdAndAuthentication(reviewId, authentication);
        oriReview.updateReview(reviewInputDto);
        List<ReviewImgDto> reviewImgDtoList = reviewImgService.edit(oriReview, deleteImgList, multipartFiles);
        return convertDto(oriReview, reviewImgDtoList);
    }

    public Review findByIdAndAuthentication(long reviewId, Authentication authentication) {
        List<Review> reviews = reviewRepository.findAllByMember(findMemberByAuthentication(authentication));
        return reviews.stream().filter(r -> r.getId() == reviewId).findAny().orElseThrow();
    }

    public void delete(long reviewId, Authentication authentication) {
        Review oriReview = findByIdAndAuthentication(reviewId, authentication);
        orderProductService.editReviewStatus(oriReview.getOrderProduct(), ReviewStatus.DELETE);
        reviewRepository.delete(oriReview);
    }

    public List<ReviewOutputDto> findAllByAuthentication(Authentication authentication) {
        Member member = findMemberByAuthentication(authentication);

        return reviewRepository.findAllByMember(member).stream().map(review -> {
            List<ReviewImg> reviewImgList = reviewImgService.findAllByReview(review);
            return convertDto(review, reviewImgService.convertDtoList(reviewImgList));
        }).toList();
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

    public Page<ReviewOutputDto> findAllByProductId(long productId, Pageable pageable) {
        List<ReviewStatus> reviewStatuses = List.of(ReviewStatus.WRITTEN, ReviewStatus.ACCRUAL);
        List<OrderProduct> orderProducts =
                orderProductService.findAllByProductIdAndReviewStatus(productId, reviewStatuses);
        return reviewRepository.findAllByOrderProductList(orderProducts, pageable)
                .map(review -> {
                    List<ReviewImg> reviewImgList = reviewImgService.findAllByReview(review);
                    return convertDto(review, reviewImgService.convertDtoList(reviewImgList));
                });
    }

    public List<ReviewOutputDto> convertDtoList(List<Review> reviews) {
        return reviews.stream().map(review -> {
            List<ReviewImg> reviewImgList = reviewImgService.findAllByReview(review);
            return convertDto(review, reviewImgService.convertDtoList(reviewImgList));
        }).toList();
    }

    public ReviewOutputDto convertDto(Review review, List<ReviewImgDto> reviewImgDtoList) {
        return ReviewOutputDto.builder()
                .id(review.getId())
                .username(review.getMember().getUsername())
                .score(review.getScore())
                .title(review.getTitle())
                .content(review.getContent())
                .regDate(review.getRegDate())
                .orderProductId(review.getOrderProduct().getId())
                .reviewImgDtoList(reviewImgDtoList)
                .build();
    }

    public Review toEntity(ReviewInputDto reviewInputDto, OrderProduct orderProduct, Member member) {
        return Review.builder()
                .score(reviewInputDto.getScore())
                .title(reviewInputDto.getTitle())
                .content(reviewInputDto.getContent())
                .regDate(LocalDateTime.now())
                .orderProduct(orderProduct)
                .member(member)
                .build();
    }

}
