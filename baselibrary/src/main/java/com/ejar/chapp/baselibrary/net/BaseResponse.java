package com.ejar.chapp.baselibrary.net;

/**
 * Created by Administrator on 2018\1\3 0003.
 */

public class BaseResponse<T> {
    private int code;
    private String message;
    private T content;
    private boolean error;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
