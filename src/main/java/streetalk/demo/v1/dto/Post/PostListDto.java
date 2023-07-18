package streetalk.demo.v1.dto.Post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import streetalk.demo.v1.domain.Post;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostListDto {
    private Long postId;
    private String title;
    private String content;
    private String location;
    private String writer;
    private Long lastTime;
    private Long likeCount;
    private Long scrapCount;
    private Long replyCount;
    private Boolean hasAuthority;
    private Long writerId;
    private Boolean isPrivate;
    private LocalDate createTime;
    private Boolean postLike;
    private Boolean postScrap;

    public PostListDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.location = post.getLocation().getSmallLocation();
        this.writer = post.getWriter();
        this.lastTime = Duration.between(post.getCreatedDate(), LocalDateTime.now()).getSeconds();
        this.likeCount = post.getLikeCount();
        this.scrapCount = post.getScrapCount();
        this.replyCount = post.getReplyCount();
        this.writerId = post.getId();
        this.isPrivate = post.getIsPrivate();
        this.createTime = post.getCreatedDate().toLocalDate();
    }
}
