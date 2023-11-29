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

}
