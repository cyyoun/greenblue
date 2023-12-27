package cyy.greenblue.service;

import cyy.greenblue.domain.Category;
import cyy.greenblue.dto.CategoryDto;
import cyy.greenblue.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private static final int CATEGORY_QUANTITY = 30;

    public Category duplicateChk(Category category) { //이름, Parent 중복인 경우 확인
        return categoryRepository.findByNameAndParent(category.getName(), category.getParent());
    }

    public int quantityChk(Category category) {
        return categoryRepository.findAllByParent(category.getParent()).size();
    }  
    
    private void depthValue(Category category) { //카테고리 depth 자동 설정 (부모 id의 깊이 + 1)
        if (category.getParent() != null) {
            Category one = findOne(category.getParent().getId());
            category.updateDepth(one.getDepth() + 1);
        }
    }

    public CategoryDto add(Category category) {
        if (duplicateChk(category) != null) {
            throw new RuntimeException("이미 해당 카테고리가 존재합니다.");
        } else if (quantityChk(category) >= CATEGORY_QUANTITY) {
            throw new RuntimeException("카테고리 수가 " + CATEGORY_QUANTITY + "개를 넘습니다.");
        } else {
            depthValue(category);
            categoryRepository.save(category);
            Category findCategory = findOne(category.getId());
            return new CategoryDto().toDto(findCategory);
        }
    }

    public CategoryDto edit(Category category){
        if (duplicateChk(category) != null) {
            throw new RuntimeException("이미 해당 카테고리가 존재합니다.");
        } else {
            depthValue(category);
            findOne(category.getId()).updateName(category.getName());
            return new CategoryDto().toDto(category);
        }
    }

    public void delete(int categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public Category findOne(int categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    public List<CategoryDto> findAllByDepth(int depth) {
        List<Category> list = categoryRepository.findAllByDepth(depth);
        return toDtoList(list);
    }

    public List<CategoryDto> findByParent(int categoryId) {
        List<Category> list = categoryRepository.findAllByParentId(categoryId);
        return toDtoList(list);
    }

    public List<CategoryDto> toDtoList(List<Category> list) {
        return list.stream()
                .map(category -> new CategoryDto().toDto(category))
                .collect(Collectors.toList());
    }
}
