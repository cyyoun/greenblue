package cyy.greenblue.repository;

import cyy.greenblue.domain.Category;
import cyy.greenblue.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCategory(Category category);

}
