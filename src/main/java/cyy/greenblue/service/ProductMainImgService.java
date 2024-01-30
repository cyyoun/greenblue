package cyy.greenblue.service;

import cyy.greenblue.domain.Product;
import cyy.greenblue.domain.ProductMainImg;
import cyy.greenblue.dto.ProductMainImgDto;
import cyy.greenblue.exception.NoMainImgException;
import cyy.greenblue.repository.ProductMainImgRepository;
import cyy.greenblue.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@Transactional
@RequiredArgsConstructor
public class ProductMainImgService {
    @Value("${file.dir.product}")
    private String fileDir;
    private final ProductMainImgRepository productMainImgRepository;
    private final FileUtil fileUtil;

    public ProductMainImgDto save(Product product, MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new NoMainImgException("상품 메인 이미지가 없습니다.");
        }
        String filename = fileUtil.saveFile(multipartFile, true, fileDir);
        ProductMainImg productMainImg = productMainImgRepository.save(new ProductMainImg(filename, product));
        return getProductMainImgDto(productMainImg);
    }

    public void delete(Product product) {
        ProductMainImg productMainImg = findOne(product);
        fileUtil.deleteFile(productMainImg.getFilename(), fileDir);
        productMainImgRepository.deleteByProduct(product);
    }

    public ProductMainImgDto edit(Product product, MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) { //변경 없음
            return getProductMainImgDto(findOne(product));
        }
        ProductMainImg productMainImg = findOne(product);
        fileUtil.deleteFile(productMainImg.getFilename(), fileDir);
        String filename = fileUtil.saveFile(multipartFile, true, fileDir);
        productMainImg.update(filename);
        return getProductMainImgDto(productMainImg);
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

