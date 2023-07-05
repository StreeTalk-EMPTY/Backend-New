package streetalk.demo.v1.exception;

import lombok.Getter;

@Getter
public class ArithmeticException extends RuntimeException{
    private int status;
    private String message;

    public ArithmeticException(int status, String message){
        this.status = status;
        this.message = message;
    }
}
