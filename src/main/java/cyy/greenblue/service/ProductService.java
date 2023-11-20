package cyy.greenblue.service;

import cyy.greenblue.domain.Category;
import cyy.greenblue.domain.Product;
import cyy.greenblue.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public Product add(Product product) {
        productRepository.save(product);
        return findOne(product.getId());
    }

    public Product edit(Product product) {
        Product oriProduct = findOne(product.getId());
        oriProduct.update(
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                product.getDescription(),
                product.getCategory()
        );
        return oriProduct;
    }

    public void delete(long productId) {
        productRepository.delete(findOne(productId));
    }

    public Product findOne(long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> findAllByCategory(String soldOut, int price1, int price2, int categoryId, Pageable pageable) {
        Category category = categoryService.findOne(categoryId);
        if (soldOut.equals("n")) { //품절 제외
            return productRepository.soldOut_N(price1, price2, category, pageable);
        }
        return productRepository.soldOut_Y(price1, price2, category, pageable); //품절 포함
    }

    public List<Product> findByWord(String word) {
        return productRepository.findAllByWord(word);
    }
}
