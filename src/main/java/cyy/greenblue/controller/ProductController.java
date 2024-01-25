package cyy.greenblue.controller;

import cyy.greenblue.dto.ProductExtendDto;
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

    @GetMapping("/{productId}")
    public ProductExtendDto detailsView(@PathVariable long productId) {
        return productService.findProductDtoById(productId);
    }

    @GetMapping("/list")
    public Page<ProductExtendDto> listByCategory(
            @RequestParam(name = "category-id") int categoryId,
            @RequestParam(name = "sort", defaultValue = "new") String sortBy,
            @RequestParam(name = "start-price", defaultValue = "0") int startPrice,
            @RequestParam(name = "end-price", defaultValue = "9999999") int endPrice,
            @RequestParam(name = "sold-out", defaultValue = "y") String soldOut, //기본 품절 포함, y:품절 , n:품절 아님
            @PageableDefault(size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Pageable newPageable = productService.dynamicPageable(sortBy, pageable);
        return productService.findAllByCategory(soldOut, startPrice, endPrice, categoryId, newPageable);
    }

    @GetMapping
    public List<ProductExtendDto> search(@RequestParam String word) {
        return productService.findByWord(word);
    }
}
