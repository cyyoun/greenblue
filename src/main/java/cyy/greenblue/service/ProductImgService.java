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
import java.util.stream.Collectors;

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

    public List<String> findFilenames(long productId) { //파일 조회
        Product product = productService.findOne(productId);
        return productImageRepository.findFilenames(product);
    }

    public void deleteFiles(List<ProductImg> productImgs) {
        List<String> filenames = productImgs.stream()
                .map(productImg -> productImg.getFilename())
                .collect(Collectors.toList());
        fileStore.deleteFiles(filenames, fileDir); //파일 삭제
        productImageRepository.deleteAll(productImgs);
    }
}
