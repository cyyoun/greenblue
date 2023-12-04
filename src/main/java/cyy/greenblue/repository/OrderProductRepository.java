package cyy.greenblue.repository;

import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.OrderSheet;
import cyy.greenblue.domain.status.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    @Query("SELECT o FROM OrderProduct o WHERE o.orderSheet = :orderSheet")
    List<OrderProduct> findByOrderSheet(OrderSheet orderSheet);

    @Query("SELECT o FROM OrderProduct o WHERE o.member.id = :memberId")
    List<OrderProduct> findByMemberId(long memberId);

    @Query("SELECT o FROM OrderProduct o " +
            "WHERE o.product.id = :productId " +
            "AND o.reviewStatus IN(:reviewStatuses)")
    List<OrderProduct> findByProductId(long productId, List<ReviewStatus> reviewStatuses);

    @Query("SELECT o FROM OrderProduct o " +
            "WHERE o.purchaseDate <= :before14Days " +
            "AND o.reviewStatus = :reviewStatus")
    List<OrderProduct> findByTimeAndReviewStatus(LocalDateTime before14Days, ReviewStatus reviewStatus);
}
