package cyy.greenblue.controller;

import cyy.greenblue.domain.Review;
import cyy.greenblue.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{orderProductId}")
    public Review save(@RequestBody Review review, @PathVariable long orderProductId) {
        reviewService.add(review, orderProductId);
        return reviewService.findOne(review.getId());
    }

}
