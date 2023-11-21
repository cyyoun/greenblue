package cyy.greenblue.service;

import cyy.greenblue.domain.Product;
import cyy.greenblue.domain.ProductImg;
import cyy.greenblue.repository.ProductImgRepository;
import cyy.greenblue.store.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductImgService {

    @Value("${file.dir.product}")
    private String fileDir;

    private final ProductImgRepository productImageRepository;
    private final ProductService productService;
    private final FileStore fileStore;

    public List<ProductImg> save(List<MultipartFile> multipartFiles, long productId) {
        Product product = productService.findOne(productId);
        List<ProductImg> saveProductImgs = new ArrayList<>();
        List<String> saveFiles;

        try {
             saveFiles = fileStore.saveFiles(multipartFiles, fileDir);//파일 이름 저장 List
            for (String saveFile : saveFiles) {
                ProductImg productImg = new ProductImg(saveFile, product);
                saveProductImgs.add(productImg);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return productImageRepository.saveAll(saveProductImgs);
    }

    public ProductImg findOne(long productImageId) {
        return productImageRepository.findById(productImageId).orElseThrow();
    }

    public List<String> findAllByProduct(long productImageId) {
        Product product = productService.findOne(productImageId);
        return productImageRepository.findAllByProduct(product);
    }

}
