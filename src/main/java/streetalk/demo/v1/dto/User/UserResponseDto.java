package streetalk.demo.v1.dto.User;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import streetalk.demo.v1.dto.NearCity;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String userName;
    private String location;
    private String industry;
}
