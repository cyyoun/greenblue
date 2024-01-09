package cyy.greenblue.util;

import cyy.greenblue.exception.ImgSaveFailException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileUtil {

    public String extractExtension(String fileName) { //확장자 추출
        int lastedIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastedIndex);
    }

    public String changeFileName(String fileName) { //파일명 UUID 로 변경
        String extension = extractExtension(fileName);
        return UUID.randomUUID().toString() + extension;
    }

    public String saveFile(MultipartFile multipartFile, boolean isMainImg, String fileDir) { //파일 저장
        if (multipartFile.isEmpty()) {
            return null;
        }
        String filename = multipartFile.getOriginalFilename(); //원래 파일명
        String changeFileName = changeFileName(filename); //저장할 파일명
        if (isMainImg) {
            changeFileName = "Main_" + changeFileName;
        }
        String fullPath = fileDir + changeFileName; //저장 파일 풀경로
        try {
            multipartFile.transferTo(new File(fullPath)); //저장하고 업로드
        } catch (IOException e) {
            throw new ImgSaveFailException(e);
        }
        return changeFileName;
    }

    public List<String> saveFiles(List<MultipartFile> multipartFiles, String fileDir) {
        List<String> savedFiles = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String savedFile = saveFile(multipartFile, false, fileDir);
            savedFiles.add(savedFile);
        }
        return savedFiles;
    }

    public void deleteFile(String filename, String fileDir) {
        File file = new File(fileDir + filename);
        if (file.exists()) {
            file.delete();
        }
    }

    public void deleteFiles(List<String> filenames, String fileDir) {
        for (String filename : filenames) {
            deleteFile(filename, fileDir);
        }
    }
}
