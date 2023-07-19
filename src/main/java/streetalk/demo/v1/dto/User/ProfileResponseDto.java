package streetalk.demo.v1.dto.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import streetalk.demo.v1.dto.NearCity;

import java.util.List;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
public class ProfileResponseDto {
    private String userName;
//    private String location;
    private String industry;
    private String currentCity;
    private List<NearCity> nearCities;
}
