package cyy.greenblue.controller.admin;

import cyy.greenblue.domain.Product;
import cyy.greenblue.domain.ProductImg;
import cyy.greenblue.dto.ProductDto;
import cyy.greenblue.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @PostMapping
    public ProductDto save(@RequestPart Product product,
                           @RequestPart List<MultipartFile> multipartFiles,
                           @RequestPart MultipartFile mainImg) {
        return productService.add(product, multipartFiles, mainImg);
    }

    @PatchMapping("{productId}")
    public ProductDto edit(@PathVariable long productId,
                           @RequestPart Product product,
                           @RequestPart(required = false) List<ProductImg> deleteImgList,
                           @RequestPart(required = false) List<MultipartFile> multipartFiles,
                           MultipartFile mainImg) {
        return productService.edit(productId, product, deleteImgList, multipartFiles, mainImg);
    }

    @DeleteMapping("{productId}")
    public String delete(@PathVariable long productId) {
        productService.delete(productId);
        return "ok";
    }

}
