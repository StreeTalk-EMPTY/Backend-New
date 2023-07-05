package streetalk.demo.v1.exception;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ErrorResponse {
    private int status;
    private String message;

}
