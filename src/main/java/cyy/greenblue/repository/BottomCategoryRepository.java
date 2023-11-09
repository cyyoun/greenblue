package cyy.greenblue.repository;

import cyy.greenblue.domain.BottomCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BottomCategoryRepository extends JpaRepository<BottomCategory, Integer> {

    public List<BottomCategory> findAllByTopCategoryId(int topCategoryId);

}

