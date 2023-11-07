package cyy.greenblue.repository;

import cyy.greenblue.domain.TopCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopCategoryRepository extends JpaRepository<TopCategory, Integer> {

    public TopCategory findByName(String name);
}
