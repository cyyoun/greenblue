package cyy.greenblue.service;

import cyy.greenblue.domain.Category;
import cyy.greenblue.dto.CategoryDto;
import cyy.greenblue.exception.CategoryException;
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
    private static final int CATEGORY_QUANTITY = 3;

    public void categoryCheck(Category category) {
        if (duplicateChk(category) != null) {
            throw new CategoryException("이미 해당 카테고리가 존재합니다.");
        } else if (quantityChk(category) >= CATEGORY_QUANTITY) {
            throw new CategoryException("카테고리 수가 " + CATEGORY_QUANTITY + "개를 넘습니다.");
        }
    }

    public Category duplicateChk(Category category) { //이름, Parent 중복인 경우 확인
        return categoryRepository.findByNameAndParent(category.getName().trim(), category.getParent());
    }

    public int quantityChk(Category category) {
        return categoryRepository.findAllByParent(category.getParent()).size();
    }

    private int findDepthValue(Category category) { //카테고리 depth 자동 설정 (부모 id의 깊이 + 1)
        if (category.getParent() != null) {
            Category one = findOne(category.getParent().getId());
            return one.getDepth() + 1;
        } else {
            return 1;
        }
    }

    public CategoryDto add(CategoryDto categoryDto) {
        Category category = convertToEntity(categoryDto);
        categoryCheck(category);
        category.updateDepth(findDepthValue(category));
        return convertToDto(categoryRepository.save(category));
    }

    public CategoryDto edit(CategoryDto categoryDto) {
        Category category = findOne(categoryDto.getId());
        Category convertCategory = convertToEntity(categoryDto);
        categoryCheck(convertCategory);
        category.updateName(convertCategory.getName());
        return convertToDto(category);
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
                .map(category -> convertToDto(category))
                .collect(Collectors.toList());
    }

    public CategoryDto convertToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .build();
    }

    public Category convertToEntity(CategoryDto categoryDto) {
        Category parent = null;
        int depth = 1;
        if (categoryDto.getParentId() != null) {
            parent = Category.builder().id(categoryDto.getParentId()).build();
             depth = findOne(categoryDto.getParentId()).getDepth();
        }

        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .depth(depth)
                .parent(parent)
                .build();
    }
}
