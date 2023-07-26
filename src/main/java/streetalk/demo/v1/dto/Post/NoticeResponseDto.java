package streetalk.demo.v1.dto.Post;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoticeResponseDto {

    private Long id;
    private String title;
    private String content;
    private LocalDate createDate;

}
