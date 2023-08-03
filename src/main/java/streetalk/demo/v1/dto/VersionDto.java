package streetalk.demo.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class VersionDto {
    private final String minimum = "1.0.10";
    private final String latest = "1.0.11";
}
