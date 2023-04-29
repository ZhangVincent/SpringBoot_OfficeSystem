package com.zkp.common.result;

import lombok.Getter;

/**
 * 返回数据状态码
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(200, "成功"),
    FAIL(201, "失败"),

    LOGIN_ERROR(204,"认证失败，请重新登陆"),
    ACCOUNT_STOP(205,"用户被禁用"),
    PERMISSION(206,"没有操作的权限");

    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
