package cyy.greenblue.repository;

import cyy.greenblue.domain.Cart;
import cyy.greenblue.domain.Member;
import cyy.greenblue.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByMemberAndProduct(Member member, Product product);

    List<Cart> findByMember(Member member);
}
