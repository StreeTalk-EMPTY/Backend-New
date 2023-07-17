package streetalk.demo.v1.dto.Post;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyResponseDto {
    private Long replyId;
    private String replyWriterName;
    private Long replyWriterId;
    private String location;
    private String content;
    private Long lastTime;
    private Boolean hasAuthority;
    private Long writerId;
    private Boolean isPrivate;
//    private String name;
}
