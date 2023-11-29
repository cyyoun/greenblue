package cyy.greenblue.controller;

import cyy.greenblue.domain.Review;
import cyy.greenblue.dto.ReviewDto;
import cyy.greenblue.service.PointService;
import cyy.greenblue.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/{productId}/review")
public class ReviewController {
    private final ReviewService reviewService;

    private final ModelMapper modelMapper;
    private final PointService pointService;

    @Transactional
    @PostMapping
    public ResponseEntity<Object> save(@RequestPart Review review, @RequestPart List<MultipartFile> multipartFiles) {
        Review saveReview = reviewService.add(review, multipartFiles);
        pointService.addReviewPoint(saveReview);
        return ResponseEntity.status(HttpStatus.OK).body(saveReview);
    }

    @PostMapping("/{reviewId}")
    public ResponseEntity<Object> edit(@RequestPart Review review, @PathVariable long reviewId,
                                       @RequestPart List<MultipartFile> multipartFiles) {
        Review editReview = reviewService.edit(review, reviewId, multipartFiles);
        ReviewDto reviewDto = modelMapper.map(editReview, ReviewDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(reviewDto);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Object> delete(@PathVariable long reviewId) {
        reviewService.delete(reviewId);
        return ResponseEntity.status(HttpStatus.OK).body("리뷰가 삭제되었습니다.");
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Object> oneReview(@PathVariable long reviewId) {
        Review review = reviewService.findOne(reviewId);
        ReviewDto reviewDto = modelMapper.map(review, ReviewDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(reviewDto);
    }

    @GetMapping("/reviews")
    public ResponseEntity<Object> allReview(
            @PathVariable long productId,
            @RequestParam(name = "sort", defaultValue = "new") String sortBy,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Pageable dynamicPageable = pageable;

        if (sortBy.equals("low-score")) {
            dynamicPageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("score").ascending());
        } else if (sortBy.equals("high-score")) {
            dynamicPageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("score").descending());
        } else if (sortBy.equals("new")) {
            dynamicPageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("id").descending()
            );
        }

        List<Review> reviews = reviewService.findAllByProductId(productId, dynamicPageable);
        List<ReviewDto> reviewsDto = reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDto.class)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(reviewsDto);
    }
}
