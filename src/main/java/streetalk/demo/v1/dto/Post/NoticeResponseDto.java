package streetalk.demo.v1.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeResponseDto {

    private String title;
    private String content;
    private LocalDate createDate;

}
