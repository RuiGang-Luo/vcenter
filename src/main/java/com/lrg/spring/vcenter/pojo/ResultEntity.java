package com.lrg.spring.vcenter.pojo;

/**
 * @author LRG
 * api接口统一返回格式
 */

public class ResultEntity {
    private String message;
    private int code;
    private Object data;

    public ResultEntity(){

    }

    public ResultEntity(int code){
        this.code = code;
    }

    public ResultEntity(int code,String message){
        this.code = code;
        this.message = message;
    }

    public ResultEntity(int code,String message,Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
