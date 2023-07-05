package streetalk.demo.v1.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class LocationDto {
    private String bigLocation;
    private String middleLocation;
    private String smallLocation;
    private Long id;
}
