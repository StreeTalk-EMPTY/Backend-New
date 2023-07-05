package streetalk.demo.v1.dto.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import streetalk.demo.v1.domain.Industry;
import streetalk.demo.v1.domain.Location;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PutUserRequestDto {
    private String name;
    private String location;
    private String industry;
}
