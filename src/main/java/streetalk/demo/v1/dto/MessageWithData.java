package streetalk.demo.v1.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class MessageWithData {
    private Integer status;
    private Boolean success;
    private String message;
    private Object data;
}
