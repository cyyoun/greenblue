package cyy.greenblue.controller;

import cyy.greenblue.domain.BottomCategory;
import cyy.greenblue.service.BottomCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bottom-category")
public class BottomCategoryController {

    private final BottomCategoryService bottomCategoryService;

    @PostMapping
    public String save(@RequestBody BottomCategory bottomCategory) {
        BottomCategory addCategory = bottomCategoryService.add(bottomCategory);
        if (addCategory == null) {
            return "이미 등록되어 있습니다.";
        }
        return "등록되었습니다.";
    }

    @PostMapping("{bottomCategoryId}")
    public String edit(@RequestBody BottomCategory bottomCategory, @PathVariable int bottomCategoryId) {
        BottomCategory before = bottomCategoryService.findOne(bottomCategoryId);
        BottomCategory editCategory = bottomCategoryService.edit(before, bottomCategory);
        if (editCategory == null) {
            return "이미 등록되어 있습니다.";
        }
        return "수정되었습니다.";
    }

    @DeleteMapping("{bottomCategoryId}")
    public void delete(@PathVariable int bottomCategoryId) {
        bottomCategoryService.delete(bottomCategoryId);
    }

    @GetMapping("{topCategoryId}/list")
    public List<BottomCategory> bottomCategoryList(@PathVariable int topCategoryId) {
        return bottomCategoryService.findAllByTopCategoryId(topCategoryId);
    }

}
