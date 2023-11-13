package cyy.greenblue.repository;

import cyy.greenblue.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
