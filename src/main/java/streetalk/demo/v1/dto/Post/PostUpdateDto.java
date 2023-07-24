package streetalk.demo.v1.dto.Post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostUpdateDto {
    private Long postId;
    private String title;
    private String content;
//    private List<MultipartFile> multipartFiles;
}
