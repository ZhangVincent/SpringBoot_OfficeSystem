package com.zkp.common.exception;

import com.zkp.common.result.ResultCodeEnum;
import lombok.Data;

@Data
public class MyException extends RuntimeException{
    private String message;
    private Integer code;

    public MyException(Integer code,String message){
        super(message);
        this.code=code;
        this.message=message;
    }

    public MyException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code=resultCodeEnum.getCode();
        this.message= resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "MyException{" +
                "message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}
