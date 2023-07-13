package streetalk.demo.v1.dto.Post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import streetalk.demo.v1.domain.Post;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostDto {
    private Long boardId;
    private String title;
    private String content;
    private Boolean checkName;
    private List<MultipartFile> multipartFiles;
}
