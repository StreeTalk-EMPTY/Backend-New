package streetalk.demo.v1.dto.Post;

import lombok.*;
import streetalk.demo.v1.domain.Location;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScrapLikeResponseDto {
    private String name;
    private Location location;
    private String title;
    private String content;
    private Long likecount;
    private Long scrapcount;
    private Object data;
}