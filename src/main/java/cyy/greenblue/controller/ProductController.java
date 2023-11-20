package cyy.greenblue.controller;

import cyy.greenblue.domain.Product;
import cyy.greenblue.dto.UserProductDto;
import cyy.greenblue.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    @PostMapping
    public Product save(@RequestBody Product product) {
        return productService.add(product);
    }

    @PostMapping("{productId}")
    public Product edit(@RequestBody Product product) {
        return productService.edit(product);
    }

    @DeleteMapping("{productId}")
    public String delete(@PathVariable long productId) {
        productService.delete(productId);
        return "삭제되었습니다.";
    }

    @GetMapping("{productId}")
    public UserProductDto detailsView(@PathVariable long productId) {
        return modelMapper.map(productService.findOne(productId), UserProductDto.class);
    }

    @GetMapping("/list")
    public List<UserProductDto> list() {
        List<Product> products = productService.findAll();
        return products.stream()
                .map(product -> modelMapper.map(product, UserProductDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/list/{categoryId}")
    public Page<UserProductDto> listByCategory(
            @PathVariable int categoryId,
            @RequestParam(name = "sort", defaultValue = "new") String sortBy,
            @RequestParam(defaultValue = "0") int price1,
            @RequestParam(defaultValue = "0") int price2,
            @RequestParam(name = "sold-out", defaultValue = "n") String soldOut, //n: 품절 제외, y: 품절 포함
            @PageableDefault(size = 80, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Pageable dynamicPageable = pageable;

        if (sortBy.equals("new")) { //최신순
            dynamicPageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by("id").descending());
        } else if (sortBy.equals("low-price")) { //낮은 가격순
            dynamicPageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by("price").ascending());
        } else if (sortBy.equals("high-price")) { //높은 가격순
            dynamicPageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by("price").descending());
        }

        return productService.findAllByCategory(soldOut, price1, price2, categoryId, dynamicPageable)
                .map(product -> modelMapper.map(product, UserProductDto.class));
    }

}
