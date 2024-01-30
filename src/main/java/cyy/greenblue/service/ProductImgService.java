package cyy.greenblue.service;

import cyy.greenblue.domain.Product;
import cyy.greenblue.domain.ProductImg;
import cyy.greenblue.dto.ProductImgDto;
import cyy.greenblue.exception.NoMainImgException;
import cyy.greenblue.repository.ProductImgRepository;
import cyy.greenblue.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductImgService {

    @Value("${file.dir.product}")
    private String fileDir;
    private final ProductImgRepository productImgRepository;
    private final FileUtil fileUtil;

    public List<ProductImgDto> save(Product product, List<MultipartFile> multipartFiles) {
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            throw new NoMainImgException("상품 이미지가 없습니다.");
        }

        List<String> saveFilenames = fileUtil.saveFiles(multipartFiles, fileDir);
        List<ProductImg> productImgList = saveFilenames.stream()
                .map(saveFile -> new ProductImg(saveFile, product)).toList();
        productImgRepository.saveAll(productImgList); //DB 저장
        return getProductImgDtoList(productImgList);
    }

    public void delete(List<ProductImg> productImgs) {
        List<String> filenames = productImgs.stream().map(ProductImg::getFilename).toList();
        fileUtil.deleteFiles(filenames, fileDir); //파일 삭제
        productImgRepository.deleteAll(productImgs);
    }

    public List<ProductImgDto> edit(Product product,
                                    List<ProductImg> deleteImgList,
                                    List<MultipartFile> multipartFile) {
        if (deleteImgList != null) {
            delete(deleteImgList);
        }
        if (multipartFile != null) {
            save(product, multipartFile);
        }
        List<ProductImg> productImgList = findAllByProduct(product);
        return getProductImgDtoList(productImgList);
    }

    public List<ProductImg> findAllByProduct(Product product) {
        return productImgRepository.findAllByProduct(product);
    }

    public List<ProductImgDto> getProductImgDtoList(List<ProductImg> productImgList) {
        return productImgList.stream()
                .map(productImg -> new ProductImgDto().toDto(productImg))
                .toList();
    }

    public List<ProductImgDto> findDtoListByProduct(Product product) {
        List<ProductImg> productImgList = findAllByProduct(product);
        return getProductImgDtoList(productImgList);
    }
}
