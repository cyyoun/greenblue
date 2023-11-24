package cyy.greenblue.repository;

import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointRepository extends JpaRepository<Point, Long> {
    @Query("SELECT SUM(p.savedPoint) FROM Point p WHERE p.member.id = :memberId")
    int findByMember(long memberId);

    @Query("SELECT p FROM Point p WHERE p.orderProduct = :orderProduct")
    Point findByOrderProduct(OrderProduct orderProduct);
}
