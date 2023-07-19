package streetalk.demo.v1.dto.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProfileRequestDto {
    private Double longitude;
    private Double latitude;
}
