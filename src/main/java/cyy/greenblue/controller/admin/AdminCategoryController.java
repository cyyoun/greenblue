package cyy.greenblue.controller.admin;

import cyy.greenblue.dto.CategoryDto;
import cyy.greenblue.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    private static void chkBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getFieldError().getDefaultMessage());
        }
    }

    @PostMapping
    public CategoryDto save(@RequestBody @Valid CategoryDto categoryDto, BindingResult bindingResult) {
        chkBindingResult(bindingResult);
        return categoryService.add(categoryDto);
    }

    @PatchMapping
    public CategoryDto edit(@RequestBody @Valid CategoryDto categoryDto, BindingResult bindingResult) {
        chkBindingResult(bindingResult);
        return categoryService.edit(categoryDto);
    }

    @DeleteMapping("/{categoryId}")
    public String delete(@PathVariable int categoryId) {
        categoryService.delete(categoryId);
        return "ok";
    }
}
