package cyy.greenblue.controller.admin;

import cyy.greenblue.domain.Category;
import cyy.greenblue.dto.CategoryDto;
import cyy.greenblue.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> save(@RequestBody Category category) {
        CategoryDto saveCategory = categoryService.add(category);
        return ResponseEntity.status(HttpStatus.OK).body(saveCategory);
    }

    @PutMapping
    public ResponseEntity<CategoryDto> edit(@RequestBody Category category) {
        CategoryDto editCategory = categoryService.edit(category);
        return ResponseEntity.status(HttpStatus.OK).body(editCategory);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> delete(@PathVariable int categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
}
