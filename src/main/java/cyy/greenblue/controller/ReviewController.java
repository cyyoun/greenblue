package cyy.greenblue.controller;

import cyy.greenblue.dto.ReviewInputDto;
import cyy.greenblue.dto.ReviewOutputDto;
import cyy.greenblue.service.PointService;
import cyy.greenblue.service.ReviewService;
import cyy.greenblue.util.ValidationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final PointService pointService;

    @Transactional
    @PostMapping
    public ReviewOutputDto save(@Valid @RequestPart("review") ReviewInputDto reviewInputDto,
                                BindingResult bindingResult,
                                @RequestPart(required = false) List<MultipartFile> multipartFiles,
                                Authentication authentication) {
        ValidationUtil.chkBindingResult(bindingResult);
        ReviewOutputDto reviewOutputDto = reviewService.add(reviewInputDto, multipartFiles, authentication);
        pointService.addReviewPoint(reviewOutputDto.getId(), authentication);
        return reviewOutputDto;
    }

    @PostMapping("/{reviewId}")
    public ReviewOutputDto edit(@Valid @RequestPart("review") ReviewInputDto reviewInputDto,
                                BindingResult bindingResult,
                                @PathVariable long reviewId,
                                @RequestPart(required = false) List<Long> deleteImgList,
                                @RequestPart(required = false) List<MultipartFile> multipartFiles,
                                Authentication authentication) {
        ValidationUtil.chkBindingResult(bindingResult);
        return reviewService.edit(reviewInputDto, reviewId, deleteImgList, multipartFiles, authentication);
    }

    @DeleteMapping("/{reviewId}")
    public String delete(@PathVariable long reviewId, Authentication authentication) {
        reviewService.delete(reviewId, authentication);
        return "ok";
    }

    @GetMapping
    public Page<ReviewOutputDto> allReview(
            @RequestParam(name = "product-id") Long productId,
            @RequestParam(name = "sort", defaultValue = "new") String sortBy,
            @PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Pageable dynamicPageable = reviewService.dynamicPageable(sortBy, pageable);
        return reviewService.findAllByProductId(productId, dynamicPageable);
    }

    @GetMapping("/mine")
    public List<ReviewOutputDto> myReviewList(Authentication authentication) {
        return reviewService.findAllByAuthentication(authentication);
    }
}
