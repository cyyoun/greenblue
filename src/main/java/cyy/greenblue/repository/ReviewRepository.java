package cyy.greenblue.repository;

import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE orderProduct = :orderProduct")
    Review findByOrderProduct(OrderProduct orderProduct);
}
