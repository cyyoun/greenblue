package cyy.greenblue.repository;

import cyy.greenblue.domain.Review;
import cyy.greenblue.domain.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Integer> {
    List<ReviewImg> findByReview(Review review);
}
