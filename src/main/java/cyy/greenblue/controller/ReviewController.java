package cyy.greenblue.controller;

import cyy.greenblue.domain.Review;
import cyy.greenblue.dto.ReviewDto;
import cyy.greenblue.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/{productId}/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Review review) {
        Review saveReview = reviewService.add(review);
        return ResponseEntity.status(HttpStatus.OK).body(saveReview);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<Object> edit(@RequestBody Review review, @PathVariable long reviewId) {
        Review editReview = reviewService.edit(review, reviewId);
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

    @GetMapping("/list")
    public ResponseEntity<Object> allReview(@PathVariable long productId) {
        List<Review> reviews = reviewService.findAllByProductId(productId);
        List<ReviewDto> reviewsDto = reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDto.class)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(reviewsDto);
    }
}
