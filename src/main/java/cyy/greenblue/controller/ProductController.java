package cyy.greenblue.controller;

import cyy.greenblue.domain.Product;
import cyy.greenblue.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public String save(@RequestBody Product product, @PathVariable long productId) {
        System.out.println("productId = " + productId);
        productService.add(product);
        return "등록되었습니다.";
    }

    @PatchMapping
    public String edit(@RequestBody Product product, @PathVariable long productId) {
        System.out.println("productId = " + productId);
        productService.edit(product);
        return "수정되었습니다.";
    }

    @DeleteMapping
    public String delete(@PathVariable long productId) {
        Product product = productService.findOne(productId);
        productService.delete(product);
        return "삭제되었습니다.";
    }

    @GetMapping
    public Product detailsView(@PathVariable long productId) {
        return productService.findOne(productId);
    }

}
