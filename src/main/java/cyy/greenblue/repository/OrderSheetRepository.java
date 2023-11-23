package cyy.greenblue.repository;

import cyy.greenblue.domain.OrderSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderSheetRepository extends JpaRepository<OrderSheet, Long> {
    @Query("SELECT o FROM OrderSheet o WHERE o.regDate >= :hoursAgo AND o.status = :status")
    List<OrderSheet> findByRegDateAndStatus(LocalDateTime hoursAgo, String status);
}
