package cyy.greenblue.service;

import cyy.greenblue.domain.Product;
import cyy.greenblue.domain.ProductMainImg;
import cyy.greenblue.dto.ProductMainImgDto;
import cyy.greenblue.repository.ProductMainImgRepository;
import cyy.greenblue.store.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductMainImgService {
    @Value("${file.dir.product}")
    private String fileDir;
    private final ProductMainImgRepository productMainImgRepository;
    private final FileStore fileStore;

    public ProductMainImgDto save(Product product, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new RuntimeException("메인 이미지가 없습니다.");
        }
        try {
            String filename = fileStore.saveFile(multipartFile, true, fileDir);
            ProductMainImg productMainImg = productMainImgRepository.save(new ProductMainImg(filename, product));
            return getProductMainImgDto(productMainImg);
        } catch (IOException e) {
            throw new RuntimeException("메인 이미지 저장 실패");
        }
    }

    public void delete(Product product) {
        ProductMainImg productMainImg = findOne(product);
        fileStore.deleteFile(productMainImg.getFilename(), fileDir);
        productMainImgRepository.deleteByProduct(product);
    }

    public ProductMainImgDto edit(Product product, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) { //변경 없음
            return getProductMainImgDto(findOne(product));
        }
        ProductMainImg productMainImg = findOne(product);
        fileStore.deleteFile(productMainImg.getFilename(), fileDir);
        try {
            String filename = fileStore.saveFile(multipartFile, true, fileDir);
            productMainImg.update(filename);
            return getProductMainImgDto(productMainImg);
        } catch (IOException e) {
            throw new RuntimeException("메인 이미지 변경 저장 실패");
        }
    }

    public ProductMainImg findOne(Product product) {
        return productMainImgRepository.findByProduct(product);
    }

    public ProductMainImgDto getProductMainImgDto(ProductMainImg productMainImg) {
        return new ProductMainImgDto().toDto(productMainImg);
    }

    public ProductMainImgDto findDtoByProduct(Product product) {
        ProductMainImg productMainImg = findOne(product);
        return getProductMainImgDto(productMainImg);
    }
}

