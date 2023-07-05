package streetalk.demo.v1.dto.Post;

import lombok.*;
import streetalk.demo.v1.domain.BaseTimeEntity;
import streetalk.demo.v1.domain.Industry;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostElementDto extends BaseTimeEntity {
    private String title;
    private String content;
    private String name;
    private String industry;
    private Integer replyCount;
    private Integer likeCount;
    private Integer scrapCount;
}