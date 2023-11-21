package cyy.greenblue.repository;

import cyy.greenblue.domain.Product;
import cyy.greenblue.domain.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductImgRepository extends JpaRepository<ProductImg, Long> {

    @Query("SELECT p.filename FROM ProductImg p WHERE p.product = :product")
    List<String> findAllByProduct(Product product);

}
