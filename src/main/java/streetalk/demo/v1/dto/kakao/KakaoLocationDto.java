package streetalk.demo.v1.dto.kakao;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoLocationDto {
    private String region_type;
    private String code;
    private String address_name;
    private String region_1depth_name;
    private String region_2depth_name;
    private String region_3depth_name;
    private String region_4depth_name;
    private Double x;
    private Double y;
}
