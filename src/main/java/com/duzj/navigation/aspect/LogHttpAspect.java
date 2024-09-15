package com.duzj.navigation.aspect;

import com.alibaba.fastjson2.JSONObject;
import com.duzj.navigation.exceptions.SystemUserException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @Description
 * @Date 2023/11/21 14:13
 * @Created by duzengjie
 */
@Aspect
@Slf4j
@Component
public class LogHttpAspect {

    /**
     * 请求
     */
    private static final List<String> excludeResApi = Lists.newArrayList("backupRecoverByExcel", "downloadAllByExcel");
    /**
     * 返回
     */
    private static final List<String> excludeReqApi = Lists.newArrayList("backupRecoverByExcel", "downloadAllByExcel");

    @Pointcut("execution(* com.duzj.navigation.controller..*.*(..)))")
    public void LogHttpAspect(){

    }

    @Around("LogHttpAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint){

        Object proceed = null;
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        try {
            if(!excludeReqApi.contains(name)){
                Object[] args = joinPoint.getArgs();
                if(!ObjectUtils.isEmpty(args)){
                    Object arg = args[0];
                    log.info("{}接口请求:{}",name, JSONObject.toJSONString(arg));
                }
            }
            proceed = joinPoint.proceed();
             if(!excludeResApi.contains(name)){
                 log.info("{}接口返回:{}",name, JSONObject.toJSONString(proceed));
             }
        } catch (Throwable e) {
            throw new SystemUserException(e.getMessage());
        }
        return proceed;
    }
}
