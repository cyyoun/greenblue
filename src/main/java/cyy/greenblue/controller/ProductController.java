package cyy.greenblue.controller;

import cyy.greenblue.domain.Product;
import cyy.greenblue.dto.UserProductDto;
import cyy.greenblue.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
        List<UserProductDto> list = products.stream()
                .map(product -> modelMapper.map(product, UserProductDto.class))
                .collect(Collectors.toList());
        return list;
    }

}
