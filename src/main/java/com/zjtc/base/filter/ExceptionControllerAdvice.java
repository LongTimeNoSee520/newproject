package com.zjtc.base.filter;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author yuchen
 * @date 2020/4/29
 */
@RestControllerAdvice
public class ExceptionControllerAdvice {


  @ExceptionHandler(NullPointerException.class)
  public String nullPointerNotValidExceptionHandler(MethodArgumentNotValidException e){
    // 从异常对象中拿到ObjectError对象
    ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
    // 然后提取错误提示信息进行返回
    return objectError.getDefaultMessage();
  }
}
