package cyy.greenblue.controller.admin;

import cyy.greenblue.dto.CategoryDto;
import cyy.greenblue.service.CategoryService;
import cyy.greenblue.util.ValidationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto save(@RequestBody @Valid CategoryDto categoryDto, BindingResult bindingResult) {
        ValidationUtil.chkBindingResult(bindingResult);
        return categoryService.add(categoryDto);
    }

    @PatchMapping
    public CategoryDto edit(@RequestBody @Valid CategoryDto categoryDto, BindingResult bindingResult) {
        ValidationUtil.chkBindingResult(bindingResult);
        return categoryService.edit(categoryDto);
    }

    @DeleteMapping("/{categoryId}")
    public String delete(@PathVariable int categoryId) {
        categoryService.delete(categoryId);
        return "ok";
    }
}
