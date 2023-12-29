package cyy.greenblue.controller;

import cyy.greenblue.dto.ProductDto;
import cyy.greenblue.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("{productId}")
    public ProductDto detailsView(@PathVariable long productId) {
        return productService.findProductDto(productId);
    }

    @GetMapping("/{categoryId}/products")
    public Page<ProductDto> listByCategory(
            @PathVariable int categoryId,
            @RequestParam(name = "sort", defaultValue = "new") String sortBy,
            @RequestParam(defaultValue = "0") int startPrice,
            @RequestParam(defaultValue = "0") int endPrice,
            @RequestParam(name = "sold-out", defaultValue = "n") String soldOut, //n: 품절 제외, y: 품절 포함
            @PageableDefault(size = 80, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Pageable newPageable = productService.dynamicPageable(sortBy, pageable);
        return productService.findAllByCategory(soldOut, startPrice, endPrice, categoryId, newPageable);
    }

    @GetMapping
    public List<ProductDto> search(@RequestParam String word) {
        return productService.findByWord(word);
    }
}
