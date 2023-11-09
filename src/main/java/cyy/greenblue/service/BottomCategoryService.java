package cyy.greenblue.service;

import cyy.greenblue.domain.BottomCategory;
import cyy.greenblue.repository.BottomCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BottomCategoryService {

    private final BottomCategoryRepository bottomCategoryRepository;

    public BottomCategory add(BottomCategory bottomCategory) {
        if (duplicateChk(bottomCategory) == 0) {
            bottomCategoryRepository.save(bottomCategory);
            return findOne(bottomCategory.getId());
        }
        return null;
    }

    public BottomCategory edit(BottomCategory before, BottomCategory after) {
        if (duplicateChk(after) == 0) {
            BottomCategory bottomCategory = findOne(before.getId());
            bottomCategory.setName(after.getName());
            bottomCategory.setTopCategory(after.getTopCategory());
            return findOne(before.getId());
        }
        return null;
    }


    public BottomCategory findOne(int bottomCategoryId) {
        return bottomCategoryRepository.findById(bottomCategoryId).orElse(null);
    }


    public void delete(int bottomCategoryId) {
        bottomCategoryRepository.deleteById(bottomCategoryId);
    }

    public List<BottomCategory> findAllByTopCategoryId(int topCategoryId) {
        return bottomCategoryRepository.findAllByTopCategoryId(topCategoryId);
    }

    public int duplicateChk(BottomCategory bottomCategory) {
        List<BottomCategory> bottomCategories = findAllByTopCategoryId(bottomCategory.getTopCategory().getId());
        return (int) bottomCategories.stream()
                .filter(c -> c.getName().equals(bottomCategory.getName()))
                .count();
    }

}
