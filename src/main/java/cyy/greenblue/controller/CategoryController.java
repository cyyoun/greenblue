package cyy.greenblue.controller;


import cyy.greenblue.domain.Category;
import cyy.greenblue.dto.CategoryDto;
import cyy.greenblue.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @PostMapping
    public String save(@RequestBody Category category) {
        Category saveCategory = categoryService.add(category);
        return "등록되었습니다.";
    }

    @PatchMapping
    public String edit(@RequestBody Category category) {
        Category editCategory = categoryService.edit(category);
        return "수정되었습니다.";
    }

    @DeleteMapping("/{categoryId}")
    public String delete(@PathVariable int categoryId) {
        categoryService.delete(categoryId);
        return "삭제되었습니다.";
    }


    private List<CategoryDto> changeDto(List<Category> categories) {
        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/categories")
    public List<CategoryDto> categories() { //대분류 카테고리 리스트
        return changeDto(categoryService.findAllByDepth(1));
    }

    @GetMapping("/categories/{categoryId}")
    public List<CategoryDto> categories(@PathVariable int categoryId) {
        return changeDto(categoryService.findByParent(categoryId));
    }
}
