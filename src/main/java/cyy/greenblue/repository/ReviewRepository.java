package cyy.greenblue.repository;

import cyy.greenblue.domain.Member;
import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByMember(Member member);

    @Query("SELECT r FROM Review r WHERE r.orderProduct IN(:orderProducts)")
    Page<Review> findAllByOrderProductList(List<OrderProduct> orderProducts, Pageable pageable);
}
