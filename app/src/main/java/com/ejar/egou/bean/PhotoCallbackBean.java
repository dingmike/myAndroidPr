package com.ejar.egou.bean;

/**
 * 照片回调对象
 */
public class PhotoCallbackBean {
    private String ret_code;
    private PhotoMsg ret_msg;

    public static class PhotoMsg {
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public String getRet_code() {
        return ret_code;
    }

    public void setRet_code(String ret_code) {
        this.ret_code = ret_code;
    }

    public PhotoMsg getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(PhotoMsg ret_msg) {
        this.ret_msg = ret_msg;
    }
}