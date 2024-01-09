package cyy.greenblue.repository;

import cyy.greenblue.domain.Cart;
import cyy.greenblue.domain.Member;
import cyy.greenblue.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByMemberAndProduct(Member member, Product product);
}
