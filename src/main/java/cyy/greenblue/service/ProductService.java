package cyy.greenblue.service;

import cyy.greenblue.domain.Category;
import cyy.greenblue.domain.Product;
import cyy.greenblue.domain.ProductImg;
import cyy.greenblue.dto.*;
import cyy.greenblue.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductImgService productImgService;
    private final ProductMainImgService mainImgService;

    public ProductExtendDto add(ProductInputSaveDto productInputSaveDto, List<MultipartFile> multipartFiles, MultipartFile mainImg) {
        Product product = toEntity(productInputSaveDto);
        Product saveProduct = productRepository.save(product);
        List<ProductImgDto> productImgDtoList = productImgService.save(saveProduct, multipartFiles);
        ProductMainImgDto mainImgDto = mainImgService.save(product, mainImg);
        return convertProductExtendDto(saveProduct, mainImgDto, productImgDtoList);
    }

    public ProductExtendDto edit(long productId, ProductInputEditDto productInputEditDto, List<ProductImg> deleteImgList,
                                 List<MultipartFile> multipartFiles, MultipartFile mainImg) {
        Product findProduct = findOne(productId);
        Product product = toEntity(productInputEditDto);
        findProduct.update(product);
        List<ProductImgDto> productImgDtoList = productImgService.edit(findProduct, deleteImgList, multipartFiles);
        ProductMainImgDto mainImgDto = mainImgService.edit(findProduct, mainImg);
        return convertProductExtendDto(findProduct, mainImgDto, productImgDtoList);
    }

    public void editQuantity(Product product, int quantity) {
        Product oriProduct = findOne(product.getId());
        oriProduct.updateQuantity(oriProduct.getQuantity() + quantity);
    }

    public void delete(long productId) {
        Product findProduct = findOne(productId);
        productRepository.delete(findProduct);
        List<ProductImg> productImgList = productImgService.findAllByProduct(findProduct);
        productImgService.delete(productImgList);
        mainImgService.delete(findProduct);
    }

    public Product findOne(long productId) {
        return productRepository.findById(productId).orElseThrow();
    }

    public Pageable dynamicPageable(String sortBy, Pageable pageable) {
        if (sortBy.equals("new")) { //최신순
            return PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by("id").descending());
        } else if (sortBy.equals("low-price")) { //낮은 가격순
            return PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by("price").ascending());
        } else if (sortBy.equals("high-price")) { //높은 가격순
            return PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by("price").descending());
        }
        return pageable;
    }

    public Page<ProductExtendDto> findAllByCategory(String soldOut, int startPrice,
                                                    int endPrice, int categoryId, Pageable pageable) {
        Category category = categoryService.findOne(categoryId);
        Page<Product> products;
        if (soldOut.equals("y")) { //품절 포함
            products = productRepository.soldOut_Y(startPrice, endPrice, category, pageable); //품절 제외
        } else {
            products = productRepository.soldOut_N(startPrice, endPrice, category, pageable);
        }
        return products.map(product -> convertProductExtendDto(product, mainImgService.findDtoByProduct(product)));
    }

    public List<ProductExtendDto> findByWord(String word) {
        return productRepository.findAllByWord(word).stream()
                .map(product -> convertProductExtendDto(product, mainImgService.findDtoByProduct(product))).toList();
    }

    public ProductExtendDto findProductDtoById(long productId) {
        Product product = findOne(productId);
        List<ProductImgDto> productImgDtoList = productImgService.findDtoListByProduct(product);
        ProductMainImgDto mainImgDto = mainImgService.findDtoByProduct(product);
        return convertProductExtendDto(product, mainImgDto, productImgDtoList);
    }

    public ProductDetailExtendDto convertProductExtendDto(Product product, ProductMainImgDto mainImgDto,
                                                          List<ProductImgDto> productImgDtoList) {
        return ProductDetailExtendDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .regDate(product.getRegDate())
                .categoryDto(categoryService.convertToDto(product.getCategory()))
                .mainImgDto(mainImgDto)
                .productImgDtoList(productImgDtoList)
                .build();
    }

    public ProductExtendDto convertProductExtendDto(Product product, ProductMainImgDto mainImgDto) {
        return ProductExtendDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .regDate(product.getRegDate())
                .categoryDto(categoryService.convertToDto(product.getCategory()))
                .mainImgDto(mainImgDto).build();
    }

    public ProductOutputDto convertProductOutputDto(Product product, ProductMainImgDto mainImgDto) {
        return ProductOutputDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .mainImgDto(mainImgDto).build();
    }

    public Product toEntity(ProductInputSaveDto productInputSaveDto) {
        return Product.productBuilder()
                .name(productInputSaveDto.getName())
                .price(productInputSaveDto.getPrice())
                .quantity(productInputSaveDto.getQuantity())
                .description(productInputSaveDto.getDescription())
                .category(categoryService.findOne(productInputSaveDto.getCategoryId()))
                .build();
    }

    public Product toEntity(ProductInputEditDto productInputEditDto) {
        return Product.productBuilder()
                .id(productInputEditDto.getId())
                .name(productInputEditDto.getName())
                .price(productInputEditDto.getPrice())
                .description(productInputEditDto.getDescription())
                .category(categoryService.findOne(productInputEditDto.getCategoryId()))
                .build();
    }
}
