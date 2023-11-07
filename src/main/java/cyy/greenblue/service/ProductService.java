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

    public void edit(Product product) {
        productRepository.save(product);
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
