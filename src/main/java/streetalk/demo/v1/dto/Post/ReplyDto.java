package streetalk.demo.v1.dto.Post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import streetalk.demo.v1.domain.Industry;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDto {
    private Long postId;
    private String content;
    private Boolean checkName;
    private Boolean isPrivate;
}
