package cyy.greenblue.controller;

import cyy.greenblue.domain.Review;
import cyy.greenblue.domain.ReviewImg;
import cyy.greenblue.dto.ReviewDto;
import cyy.greenblue.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Transactional
    @PostMapping
    public ReviewDto save(@RequestPart Review review,
                          @RequestPart List<MultipartFile> multipartFiles,
                          Authentication authentication) {
        return reviewService.add(review, multipartFiles, authentication);
    }

    @PostMapping("/{reviewId}")
    public ReviewDto edit(@RequestPart Review review,
                          @PathVariable long reviewId,
                          @RequestPart(required = false) List<ReviewImg> deleteImgList,
                          @RequestPart(required = false) List<MultipartFile> multipartFiles,
                          Authentication authentication) {
        return reviewService.edit(review, reviewId, deleteImgList, multipartFiles, authentication);
    }

    @DeleteMapping("/{reviewId}")
    public String delete(@PathVariable long reviewId, Authentication authentication) {
        reviewService.delete(reviewId, authentication);
        return "ok";
    }

    @GetMapping
    public List<ReviewDto> allReview(
            @RequestParam(name = "product-id") Long productId,
            @RequestParam(name = "sort", defaultValue = "new") String sortBy,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Pageable dynamicPageable = reviewService.dynamicPageable(sortBy, pageable);
        return reviewService.findAllByProductId(productId, dynamicPageable);
    }
}
