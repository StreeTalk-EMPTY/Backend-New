package streetalk.demo.v1.dto.sms;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
public class SmsResponseDto {
    private String statusCode;
    private String statusName;
    private String requestId;
    private Timestamp requestTime;
}
