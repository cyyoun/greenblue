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
        productImgService.save(multipartFiles, productId);
        return "이미지 업로드 하였습니다.";
    }

    @GetMapping("/list")
    public List<String> list(@PathVariable long productId) {
        return productImgService.findFilenames(productId);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody List<ProductImg> productImgs) {
        productImgService.deleteFiles(productImgs);
    }

}
