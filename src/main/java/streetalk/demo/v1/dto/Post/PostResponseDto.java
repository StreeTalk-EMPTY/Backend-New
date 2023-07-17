package streetalk.demo.v1.dto.Post;

import lombok.*;
import streetalk.demo.v1.domain.Industry;
import streetalk.demo.v1.domain.Location;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDto {
    private String boardName;
    private String postWriterName;
    private Long postWriterId;
    private String location;
    private String industry;
    private String title;
    private String content;
    private Long likeCount;
    private Long scrapCount;
    private Long replyCount;
    private Long lastTime;
    private Boolean postLike;
    private Boolean postScrap;
    private Boolean hasAuthority;
    private Long writerId;
    private Boolean isPrivate;
    private List<ReplyResponseDto> replyList;
    private List<String> images;
    
}