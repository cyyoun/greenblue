package cyy.greenblue.repository;

import cyy.greenblue.domain.TopCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopCategoryRepository extends JpaRepository<TopCategory, Integer> {

    List<TopCategory> findAllByName(String name);
}
