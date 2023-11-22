package cyy.greenblue.repository;

import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.OrderSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    @Query("SELECT o FROM OrderProduct o WHERE o.orderSheet = :orderSheet")
    List<OrderProduct> findByOrderSheet(OrderSheet orderSheet);


}
