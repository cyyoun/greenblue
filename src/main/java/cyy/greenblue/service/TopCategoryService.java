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

    public TopCategory duplicateChk(TopCategory topCategory) {
        return topCategoryRepository.findByName(topCategory.getName());
    }

    public TopCategory add(TopCategory topCategory) {
        topCategoryRepository.save(topCategory);
        return topCategoryRepository.findById(topCategory.getId()).orElse(null);
    }

    public void edit(TopCategory topCategory) {
        topCategoryRepository.save(topCategory);
    }

    public void delete(TopCategory topCategory) {
        topCategoryRepository.delete(topCategory);
    }

    public TopCategory findOne(int topCategoryId) {
        return topCategoryRepository.findById(topCategoryId).orElse(null);
    }

    public List<TopCategory> findAll() {
        return topCategoryRepository.findAll();
    }

}
