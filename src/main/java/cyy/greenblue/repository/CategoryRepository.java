package cyy.greenblue.repository;

import cyy.greenblue.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByNameAndParent(String name, Category parent);

    List<Category> findAllByDepth(int depth);

    List<Category> findAllByParent(Category parent);

    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId")
    List<Category> findAllByParentId(int parentId);
}
