package streetalk.demo.v1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import streetalk.demo.v1.domain.Post;
import streetalk.demo.v1.domain.PostImages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PostImageService {
    private final S3Service s3Service;

    @Transactional
    public List<PostImages> setPostImages(Long userId, Post post, List<MultipartFile> multipartFiles) throws IOException {
        System.out.println("setPostImages");
        List<PostImages> postImages = new ArrayList<>();
        int count = 0;

        for (MultipartFile multipartFile : multipartFiles) {
            count ++;

            String fileName = userId + "/" + post.getId()+"/" + count;
            postImages.add(PostImages.builder()
                            .post(post)
                            .name(fileName)
                            .build());
            s3Service.upload(fileName, multipartFile);
        }
        return postImages;
    }

    @Transactional
    public List<String> getPostImagesUrl(Post post){
        return post.getImages().stream()
                .map(image -> s3Service.getPreSignedDownloadUrl(image.getName()))
                .collect(toList());
    }

}
