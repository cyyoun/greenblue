package cyy.greenblue.service;

import cyy.greenblue.domain.Product;
import cyy.greenblue.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void add(Product product) {
        productRepository.save(product);
    }

    public Product edit(long productId, Product product) {
        Product newProduct = findOne(productId);
        newProduct.setName(product.getName());
        newProduct.setCode(product.getCode());
        newProduct.setPrice(product.getPrice());
        newProduct.setColor(product.getColor());
        newProduct.setSize(product.getSize());
        newProduct.setBottomCategory(product.getBottomCategory());
        return newProduct;
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }

    public Product findOne(long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

}
