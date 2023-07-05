package streetalk.demo.v1.dto.Post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostDto {
    private String board;
    private String title;
    private String content;
    private Boolean checkName;
    private List<MultipartFile> multipartFiles;
}
