package streetalk.demo.v1.dto.User;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@ToString
public class LoginRequestDto {
    private String phoneNum;
    private Double longitude;
    private Double latitude;
    private String randomNum;
}
