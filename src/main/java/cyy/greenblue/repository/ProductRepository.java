package cyy.greenblue.repository;

import cyy.greenblue.domain.Category;
import cyy.greenblue.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.price >= :price1 AND " +
            "p.price <= :price2 AND p.category = :category")
    Page<Product> soldOut_Y(int price1, int price2, Category category, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.quantity > 0 AND p.price >= :price1 AND " +
            "p.price <= :price2 AND p.category = :category")
    Page<Product> soldOut_N(int price1, int price2, Category category, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:word%")
    List<Product> findAllByWord(String word);
}
