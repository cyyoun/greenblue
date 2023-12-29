package cyy.greenblue.repository;

import cyy.greenblue.domain.Product;
import cyy.greenblue.domain.ProductMainImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMainImgRepository extends JpaRepository<ProductMainImg, Long> {
    void deleteByProduct(Product product);

    ProductMainImg findByProduct(Product product);
}
