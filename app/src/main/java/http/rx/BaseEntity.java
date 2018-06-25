package http.rx;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/25.
 */

//FIXME 统一的网络请求实体类
public class BaseEntity<T> implements Serializable {

    private int code;//网络状态
    private int errorCode;//错误码
    private String errorMessage;//错误信息
    private T data;//请求返回的实体类

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
