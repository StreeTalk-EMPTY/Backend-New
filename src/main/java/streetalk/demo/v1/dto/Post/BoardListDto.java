package streetalk.demo.v1.dto.Post;

import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardListDto {
    List<BoardDto> boardList;
}
