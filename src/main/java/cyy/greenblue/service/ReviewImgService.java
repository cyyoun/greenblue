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
        return reviewImgList.stream().map(reviewImg -> new ReviewImgDto().toDto(reviewImg)).toList();
    }

    public List<ReviewImgDto> save(Review review, List<MultipartFile> multipartFiles) {
        if (multipartFiles.size() == 1 && multipartFiles.get(0).isEmpty()) {
            throw new RuntimeException("리뷰 첨부파일이 없습니다.");
        }
        try {
            List<String> filenames = fileUtil.saveFiles(multipartFiles, fileDir);
            List<ReviewImg> reviewImgList = filenames.stream().map(filename -> new ReviewImg(filename, review)).toList();
            reviewImgRepository.saveAll(reviewImgList);
            return convertDtoList(reviewImgList);
        } catch (Exception e) {
            throw new RuntimeException("파일을 저장하지 못했습니다.");
        }
    }

    public List<ReviewImgDto> edit(Review review,
                                List<ReviewImg> deleteImgList,
                                List<MultipartFile> multipartFiles) {
        if (deleteImgList != null) {
            deleteAll(deleteImgList);
        }
        if (multipartFiles.size() != 1 || !multipartFiles.get(0).isEmpty()) {
            save(review, multipartFiles);
        }
        return convertDtoList(findAllByReview(review));
    }

    public List<ReviewImg> findAllByReview(Review review) {
        return reviewImgRepository.findByReview(review);
    }

    public void deleteAll(List<ReviewImg> reviewImgList) {
        reviewImgRepository.deleteAll(reviewImgList);
        for (ReviewImg reviewImg : reviewImgList) {
            fileUtil.deleteFile(reviewImg.getFilename(), fileDir);
        }
        reviewImgRepository.flush();
    }
}
