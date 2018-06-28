package com.discuzmobile.my.discuzmobile.bean;

public class ResponseBean<T> {

    /**
     * respCode : 1000
     * respMsg : 成功!
     * body : {"token":"8F5ACF36872399F8","phoneNo":"153****1588","isNewUser":0,"pushKey":"9A49B391F6B2522C61D39D5E653CF33B"}
     */
    private Long code;
    private String msg;
    private T body;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", body=" + body +
                '}';
    }
}
