package cyy.greenblue.service;

import cyy.greenblue.domain.TopCategory;
import cyy.greenblue.repository.TopCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TopCategoryService {

    private final TopCategoryRepository topCategoryRepository;

    public int duplicateChk(TopCategory topCategory) {
        return topCategoryRepository.findAllByName(topCategory.getName()).size();
    }

    public TopCategory add(TopCategory topCategory) {
        if (duplicateChk(topCategory) == 0) {
            topCategoryRepository.save(topCategory);
            return findOne(topCategory.getId());
        }
        return null;
    }

    public TopCategory edit(TopCategory topCategory) {
        if (duplicateChk(topCategory) == 0) {
            topCategoryRepository.save(topCategory);
            return findOne(topCategory.getId());
        }
        return null;
    }

    public void delete(int topCategoryId) {
        topCategoryRepository.deleteById(topCategoryId);
    }

    public TopCategory findOne(int topCategoryId) {
        return topCategoryRepository.findById(topCategoryId).orElse(null);
    }

    public List<TopCategory> findAll() {
        return topCategoryRepository.findAll();
    }

}
