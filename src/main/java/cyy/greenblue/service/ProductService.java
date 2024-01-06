package cyy.greenblue.service;

import cyy.greenblue.domain.Category;
import cyy.greenblue.domain.Product;
import cyy.greenblue.domain.ProductImg;
import cyy.greenblue.dto.ProductDto;
import cyy.greenblue.dto.ProductImgDto;
import cyy.greenblue.dto.ProductMainImgDto;
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

    public ProductDto getProductDto(Product product, ProductMainImgDto mainImgDto, List<ProductImgDto> productImgDtoList) {
        return new ProductDto().toDto(product, mainImgDto, productImgDtoList);
    }


    public ProductDto add(Product product, List<MultipartFile> multipartFiles, MultipartFile mainImg) {
        Product saveProduct = productRepository.save(product);
        List<ProductImgDto> productImgDtoList = productImgService.save(saveProduct, multipartFiles);
        ProductMainImgDto mainImgDto = mainImgService.save(product, mainImg);
        return getProductDto(saveProduct, mainImgDto, productImgDtoList);
    }

    public ProductDto edit(long productId, Product product,
                           List<ProductImg> deleteImgList,
                           List<MultipartFile> multipartFiles,
                           MultipartFile mainImg) {
        Product findProduct = findOne(productId);
        findProduct.update(product);
        List<ProductImgDto> productImgDtoList = productImgService.edit(findProduct, deleteImgList, multipartFiles);
        ProductMainImgDto mainImgDto = mainImgService.edit(findProduct, mainImg);
        return getProductDto(findProduct, mainImgDto, productImgDtoList);
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

    public ProductDto findProductDto(long productId) {
        Product product = findOne(productId);
        List<ProductImgDto> productImgDtoList = productImgService.findDtoListByProduct(product);
        ProductMainImgDto mainImgDto = mainImgService.findDtoByProduct(product);
        return getProductDto(product, mainImgDto, productImgDtoList);
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

    public List<ProductDto> findAllByCategory(String soldOut, int startPrice,
                                              int endPrice, int categoryId, Pageable pageable) {
        Category category = categoryService.findOne(categoryId);
        Page<Product> products;
        if (soldOut.equals("y")) { //품절 포함
            products = productRepository.soldOut_Y(startPrice, endPrice, category, pageable); //품절 제외
        } else {
            products = productRepository.soldOut_N(startPrice, endPrice, category, pageable);
        }
        return products.map(product ->new ProductDto()
                .toDto(product, mainImgService.findDtoByProduct(product))).toList();

    }

    public List<ProductDto> findByWord(String word) {
        return productRepository.findAllByWord(word).stream()
                .map(product -> new ProductDto().toDto(product, mainImgService.findDtoByProduct(product)))
                .toList();

    }
}
