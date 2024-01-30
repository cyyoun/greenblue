package cyy.greenblue.service;

import cyy.greenblue.domain.Review;
import cyy.greenblue.domain.ReviewImg;
import cyy.greenblue.dto.ReviewImgDto;
import cyy.greenblue.repository.ReviewImgRepository;
import cyy.greenblue.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewImgService {

    @Value("${file.dir.review}")
    private String fileDir;
    private final ReviewImgRepository reviewImgRepository;
    private final FileUtil fileUtil;

    public List<ReviewImgDto> convertDtoList(List<ReviewImg> reviewImgList) {
        return reviewImgList.stream().map(this::convertDto).toList();
    }

    public List<ReviewImgDto> save(Review review, List<MultipartFile> multipartFiles) {
        if (multipartFiles  == null) {
            return new ArrayList<>();
        }
        List<String> filenames = fileUtil.saveFiles(multipartFiles, fileDir);
        List<ReviewImg> reviewImgList = filenames.stream().map(filename -> convertEntity(filename, review)).toList();
        reviewImgRepository.saveAll(reviewImgList);
        return convertDtoList(reviewImgList);
    }

    public List<ReviewImgDto> edit(Review review,
                                List<Long> deleteImgList,
                                List<MultipartFile> multipartFiles) {
        System.out.println(deleteImgList != null);
        if (deleteImgList != null) {
            deleteAll(deleteImgList);
        }
        if (multipartFiles == null) {
            save(review, multipartFiles);
        }
        return convertDtoList(findAllByReview(review));
    }

    public List<ReviewImg> findAllByReview(Review review) {
        return reviewImgRepository.findByReview(review);
    }

    public void deleteAll(List<Long> deleteImgList) {
        for (long id : deleteImgList) {
            System.out.println(id);
            fileUtil.deleteFile(findOne(id).getFilename(), fileDir);
            reviewImgRepository.deleteById(id);
        }
    }

    public ReviewImg findOne(long id) {
        return reviewImgRepository.findById(id).orElseThrow();
    }

    public ReviewImgDto convertDto(ReviewImg reviewImg) {
        return ReviewImgDto.builder().id(reviewImg.getId()).filename(reviewImg.getFilename()).build();
    }

    public ReviewImg convertEntity(String filename, Review review) {
        return ReviewImg.builder().filename(filename).review(review).build();
    }
}
