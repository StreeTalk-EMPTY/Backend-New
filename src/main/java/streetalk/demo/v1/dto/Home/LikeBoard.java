package streetalk.demo.v1.dto.Home;

import lombok.*;

import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikeBoard {
    String boardName;
    Long boardId;
}
