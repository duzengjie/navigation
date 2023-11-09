package com.duzj.navigation.exceptions;

import com.duzj.navigation.entity.base.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultDTO<String> exceptionHandler(Exception e) {
        log.error("未知异常！原因是:", e);
        return ResultDTO.error("999999",e.getMessage());
    }
}