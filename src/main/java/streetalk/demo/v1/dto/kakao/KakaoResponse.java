package streetalk.demo.v1.dto.kakao;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import java.util.HashMap;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect
public class KakaoResponse {
    private HashMap<String,Integer> meta;
    private List<KakaoLocationDto> documents;
}
