package streetalk.demo.v1.dto.User;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import streetalk.demo.v1.domain.Location;
import streetalk.demo.v1.domain.User;
import streetalk.demo.v1.dto.NearCity;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private String userName;
    private Optional<String> location;
    private Optional<String> industry;
    private String currentCity;
    private List<NearCity> nearCities;
}
