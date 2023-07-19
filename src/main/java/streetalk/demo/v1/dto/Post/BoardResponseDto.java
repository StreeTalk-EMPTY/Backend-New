package streetalk.demo.v1.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class BoardResponseDto {
    private boolean isBoardLike;
    private List<PostListDto> postListDto;
}
