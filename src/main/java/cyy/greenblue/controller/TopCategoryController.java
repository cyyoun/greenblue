package cyy.greenblue.controller;


import cyy.greenblue.domain.TopCategory;
import cyy.greenblue.service.TopCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category/top")
@RequiredArgsConstructor
public class TopCategoryController {

    private final TopCategoryService topCategoryService;

    @PostMapping
    public String save(@RequestBody TopCategory topCategory) {
        TopCategory duplicateChk = topCategoryService.duplicateChk(topCategory);
        if (duplicateChk == null) { //신규 생성
            topCategoryService.add(topCategory);
            return "등록되었습니다.";
        }
        return "이미 등록되어 있습니다.";
    }

    @PatchMapping
    public String edit(@RequestBody TopCategory topCategory) {
        TopCategory duplicateChk = topCategoryService.duplicateChk(topCategory);
        if (duplicateChk == null) {
            topCategoryService.edit(topCategory);
            return "수정되었습니다.";
        }
        return "이미 등록되어 있습니다.";
    }
    @DeleteMapping(path = "{topCategoryId}")
    public String save(@PathVariable int topCategoryId) {
        TopCategory topCategory = topCategoryService.findOne(topCategoryId);
        topCategoryService.delete(topCategory);
        return "삭제되었습니다.";
    }

    @GetMapping("/list")
    public List<TopCategory> findAll() {
        return topCategoryService.findAll();
    }

}
