package com.zjtc.base.response;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * @author yuchen
 * @date 2019/11/16
 */

public class ApiResponse implements Serializable {

  private static final long serialVersionUID = 6459579728006690174L;

  /**
   * 状态码
   * */
  @ApiModelProperty("状态码")
  private int code;

  /**
   * 错误原因
   * */
  @ApiModelProperty("错误原因")
  private String message;

  /**
   * 返回结果
   * */
  @ApiModelProperty("返回结果")
  private Object data;



  public ApiResponse() {
    this.code = 200;
    this.message = "操作成功";
  }

  public ApiResponse(Object data) {
    new ApiResponse();
    this.data = data;
  }

  public void recordError(int code) {
    this.code = code;
    this.message = StatusCode.getStatusCode(code).getMessage();
  }

  public void recordError(String error) {
    this.code = 500;
    this.message = error;
  }

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

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "ApiResponse{" +
        "code='" + code + '\'' +
        ", message='" + message + '\'' +
        ", data=" + data +
        '}';
  }
}