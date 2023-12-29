package com.duzj.navigation.exceptions;

import com.duzj.navigation.entity.base.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
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
        if(e.getCause() instanceof DuplicateKeyException){
            return ResultDTO.error("999999","数据库主键冲突");
        }
        return ResultDTO.error("999999",e.getMessage());
    }

}