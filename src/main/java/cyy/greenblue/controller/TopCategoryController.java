package cyy.greenblue.controller;


import cyy.greenblue.domain.TopCategory;
import cyy.greenblue.service.TopCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/top-category")
@RequiredArgsConstructor
public class TopCategoryController {

    private final TopCategoryService topCategoryService;

    @PostMapping
    public String save(@RequestBody TopCategory topCategory) {
        if (topCategoryService.add(topCategory) == null) {
            return "이미 등록되어 있습니다.";
        }
        return "등록되었습니다.";
    }

    @PatchMapping("/{topCategoryId}")
    public String edit(@RequestBody TopCategory topCategory) {
        if (topCategoryService.edit(topCategory) == null) {
            return "이미 등록되어 있습니다.";
        }
        return "수정되었습니다.";
    }

    @DeleteMapping("/{topCategoryId}")
    public String delete(@PathVariable int topCategoryId) {
        topCategoryService.delete(topCategoryId);
        return "삭제되었습니다.";
    }

    @GetMapping("/list")
    public List<TopCategory> findAll() {
        return topCategoryService.findAll();
    }

}
