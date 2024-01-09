package cyy.greenblue.controller.admin;

import cyy.greenblue.domain.ProductImg;
import cyy.greenblue.dto.ProductInputEditDto;
import cyy.greenblue.dto.ProductInputSaveDto;
import cyy.greenblue.dto.ProductOutputDto;
import cyy.greenblue.service.ProductService;
import cyy.greenblue.util.ValidationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @PostMapping
    public ProductOutputDto save(@Valid @RequestPart(name = "product") ProductInputSaveDto productInputSaveDto,
                                 BindingResult bindingResult,
                                 @RequestPart List<MultipartFile> multipartFiles,
                                 @RequestPart MultipartFile mainImg
                                 ) {
        ValidationUtil.chkBindingResult(bindingResult);
        return productService.add(productInputSaveDto, multipartFiles, mainImg);
    }

    @PatchMapping("{productId}")
    public ProductOutputDto edit(@PathVariable long productId,
                                 @Valid @RequestPart(name = "product") ProductInputEditDto productInputEditDto,
                                 BindingResult bindingResult,
                                 @RequestPart(required = false) List<ProductImg> deleteImgList,
                                 @RequestPart(required = false) List<MultipartFile> multipartFiles,
                                 MultipartFile mainImg) {
        ValidationUtil.chkBindingResult(bindingResult);
        return productService.edit(productId, productInputEditDto, deleteImgList, multipartFiles, mainImg);
    }

    @DeleteMapping("{productId}")
    public String delete(@PathVariable long productId) {
        productService.delete(productId);
        return "ok";
    }

}
