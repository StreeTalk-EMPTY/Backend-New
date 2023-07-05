package streetalk.demo.v1.dto.Home;

import lombok.*;
import streetalk.demo.v1.domain.Post;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor

public class HomePostListDto {
    private Long postId;
    private String title;
    private String location;
    private String time;
    private Long replyCount;

    public HomePostListDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.location = post.getLocation().getSmallLocation();
        this.time = post.getCreatedDate().format(DateTimeFormatter.ofPattern("HH:MM")).toString();
        this.replyCount = post.getReplyCount();
    }
}
