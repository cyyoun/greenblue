package cyy.greenblue.service;

import cyy.greenblue.domain.Category;
import cyy.greenblue.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private static int categoryQuantity = 30;

    public Category duplicateChk(Category category) { //이름, Parent 중복인 경우 확인
        return categoryRepository.findByNameAndParent(category.getName(), category.getParent());
    }

    public int quantityChk(Category category) {
        return categoryRepository.findAllByParent(category.getParent()).size();
    }  
    
    private void depthValue(Category category) { //카테고리 depth 자동 설정 (부모 id의 깊이 + 1)
        if (category.getParent() != null) {
            Category one = findOne(category.getParent().getId());
            category.addDepth(one.getDepth() + 1);
        }
    }

    public Category add(Category category) {
        if (duplicateChk(category) != null) {
            throw new RuntimeException("이미 해당 카테고리가 존재합니다.");
        } else if (quantityChk(category) >= categoryQuantity) {
            throw new RuntimeException("카테고리 수가 " + categoryQuantity + "개를 넘습니다.");
        } else {
            depthValue(category);
            categoryRepository.save(category);
            return findOne(category.getId());
        }
    }

    public Category edit(Category category){
        if (duplicateChk(category) != null) {
            throw new RuntimeException("이미 해당 카테고리가 존재합니다.");
        } else {
            depthValue(category);
            findOne(category.getId()).editName(category.getName());
            return category;
        }
    }

    public void delete(int categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public Category findOne(int categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    public List<Category> findAllByDepth(int depth) {
        return categoryRepository.findAllByDepth(depth);
    }

    public List<Category> findByParent(int categoryId) {
        return categoryRepository.findAllByParentId(categoryId);
    }

}
