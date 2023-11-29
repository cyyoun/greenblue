package cyy.greenblue.service;

import cyy.greenblue.domain.Review;
import cyy.greenblue.domain.ReviewImg;
import cyy.greenblue.repository.ReviewImgRepository;
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
public class ReviewImgService {

    @Value("${file.dir.review}")
    private String fileDir;
    private final ReviewImgRepository reviewImgRepository;
    private final FileStore fileStore;


    public List<ReviewImg> save(Review review, List<MultipartFile> multipartFiles) {
        List<ReviewImg> reviewImgs = new ArrayList<>();
        if (multipartFiles.size() == 1 &&
                multipartFiles.get(0).getOriginalFilename().equals("")) {
            System.out.println("리뷰에 첨부 파일이 없습니다." );
            return null;
        }
        try {
            List<String> saveFiles = fileStore.saveFiles(multipartFiles, fileDir);
            for (String saveFile : saveFiles) {
                ReviewImg reviewImg = new ReviewImg(saveFile, review);
                reviewImgs.add(reviewImg);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return reviewImgRepository.saveAll(reviewImgs);
    }

    public List<ReviewImg> edit(Review review, List<MultipartFile> multipartFiles) {

        List<ReviewImg> oriReviewImgs = findAllByReview(review);
        List<String> filenames = oriReviewImgs.stream()
                .map(reviewImg -> reviewImg.getFilename())
                .collect(Collectors.toList());

        fileStore.deleteFiles(filenames, fileDir);
        reviewImgRepository.deleteAll(oriReviewImgs);
        reviewImgRepository.flush();
        return save(review, multipartFiles);
    }

    public List<ReviewImg> findAllByReview(Review review) {
        return reviewImgRepository.findByReview(review);
    }
}
