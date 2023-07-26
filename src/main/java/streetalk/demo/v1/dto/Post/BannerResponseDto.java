package streetalk.demo.v1.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BannerResponseDto {

    private String title;
    private String content;
    private boolean isNotice;
    private Long contentId;
}
