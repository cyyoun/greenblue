package cyy.greenblue.controller;

import cyy.greenblue.domain.ProductImg;
import cyy.greenblue.service.ProductImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/{productId}/image")
public class ProductImgController {

    private final ProductImgService productImgService;

    @PostMapping
    public String upload(@PathVariable long productId, @RequestBody List<MultipartFile> multipartFiles) {
        productImgService.save(productId, multipartFiles);
        return "이미지 업로드 하였습니다.";
    }

    @GetMapping("/list")
    public List<String> list(@PathVariable long productId) {
        return productImgService.findFilenames(productId);
    }

    @GetMapping("/{productImgId}")
    public String detailsView(@PathVariable long productImgId) {
        ProductImg productImg = productImgService.findOne(productImgId);
        return productImg.getFilename();
    }

    @PatchMapping("/{productImgId}")
    public String edit(@PathVariable long productImgId, @RequestParam MultipartFile multipartFile) {
        ProductImg productImg = productImgService.edit(productImgId, multipartFile);
        System.out.println(productImg.getId());
        return productImg.getFilename();
    }

    @PostMapping("/delete")
    public void delete(@RequestBody List<ProductImg> productImgs) {
        productImgService.deleteFiles(productImgs);
    }

}
