package streetalk.demo.v1.dto.User;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class AuthResponseDto {
    private String randomNum;
}
