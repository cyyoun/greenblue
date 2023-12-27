package cyy.greenblue.controller;

import cyy.greenblue.domain.Category;
import cyy.greenblue.dto.CategoryDto;
import cyy.greenblue.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> categories() { //대분류 카테고리 리스트
        List<CategoryDto> categories = categoryService.findAllByDepth(1);
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<List<CategoryDto>> categories(@PathVariable int categoryId) {
        List<CategoryDto> categories = categoryService.findByParent(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }
}
