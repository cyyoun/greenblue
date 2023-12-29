package cyy.greenblue.controller.admin;

import cyy.greenblue.domain.Category;
import cyy.greenblue.dto.CategoryDto;
import cyy.greenblue.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto save(@RequestBody Category category) {
        return categoryService.add(category);
    }

    @PatchMapping
    public CategoryDto edit(@RequestBody Category category) {
        return categoryService.edit(category);
    }

    @DeleteMapping("/{categoryId}")
    public String delete(@PathVariable int categoryId) {
        categoryService.delete(categoryId);
        return "ok";
    }
}
