package cyy.greenblue.repository;

import cyy.greenblue.domain.OrderSheet;
import cyy.greenblue.domain.status.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderSheetRepository extends JpaRepository<OrderSheet, Long> {
    @Query("SELECT o FROM OrderSheet o " +
            "WHERE :fromDateTime <= o.regDate AND o.regDate <= :toDateTime " +
            "AND o.orderStatus = :orderStatus")
    List<OrderSheet> findByTimeRange(LocalDateTime fromDateTime, LocalDateTime toDateTime, OrderStatus orderStatus);

    @Query("SELECT o FROM OrderSheet o " +
            "WHERE o.regDate <= :hoursAgo " +
            "AND o.orderStatus = :orderStatus")
    List<OrderSheet> findAfterTimeAndStatus(LocalDateTime hoursAgo, OrderStatus orderStatus);
}
