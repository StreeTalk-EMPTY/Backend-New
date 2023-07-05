package streetalk.demo.v1.service;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import streetalk.demo.v1.domain.Post;
import streetalk.demo.v1.domain.PostImages;
import streetalk.demo.v1.domain.User;
import streetalk.demo.v1.exception.ArithmeticException;
import streetalk.demo.v1.repository.UserRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
@Component
@Transactional
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    public String S3BUCKET;  // S3 버킷 이름
    @Value("${cloud.aws.region.static}")
    public Regions clientRegion;  // S3 지역

    private final UserRepository userRepository;
    private final AmazonS3Client amazonS3Client;

    @Transactional
    public void upload(String fileName, MultipartFile multipartFiles) throws IOException {
        File uploadFile = convert(multipartFiles)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));
        System.out.println("filename = " + fileName);
        uploadToS3(fileName, uploadFile);

        return;
    }



    // S3로 파일 업로드하기
    private void uploadToS3(String fileName, File uploadFile) {
        putS3(uploadFile, fileName); // s3로 업로드
        removeNewFile(uploadFile);
        return;
    }

    // S3로 업로드
    private void putS3(File uploadFile, String fileName) {
        System.out.println("start putS3");
        amazonS3Client.putObject(
                new PutObjectRequest(S3BUCKET, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
        return;
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            System.out.println("File delete success");
            return;
        }
        System.out.println("File delete fail");
        log.info("File delete fail");
    }


    // 로컬에 파일 업로드 하기
    private Optional<File> convert(MultipartFile file) throws IOException {
        System.out.println("file = " + System.getProperty("user.dir") + "/" + file.getOriginalFilename());

        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

/////////////////////////////////////////////////////////////////////////////
    //사진 가져올때 url 생성해주는 함수
    public String getPreSignedDownloadUrl(String objectKey){
        URL url;
//        return PREFIX+objectKey;

        try {
            // Set the presigned URL to expire after one hour.
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = Instant.now().toEpochMilli();
            expTimeMillis +=  1 * 60 * 60 * 5 * 1000; //5시간
            expiration.setTime(expTimeMillis);

            // Generate the presigned URL.
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(S3BUCKET, objectKey)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);
            url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
            System.out.println(url);
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            throw new ArithmeticException(404, "can't process S3"+e);
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            throw new ArithmeticException(404, "can't contract S3"+e);
        }
        return url.toString();
    }
}