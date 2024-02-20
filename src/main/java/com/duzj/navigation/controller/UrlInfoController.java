package com.duzj.navigation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duzj.navigation.entity.UrlInfo;
import com.duzj.navigation.entity.UrlInfoChangeLog;
import com.duzj.navigation.entity.base.ResultDTO;
import com.duzj.navigation.entity.request.UrlInfoRequest;
import com.duzj.navigation.service.UrlInfoChangeLogService;
import com.duzj.navigation.service.UrlInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Date 2023/11/9 15:39
 * @Created by duzengjie
 */
@RestController
@RequestMapping("/url")
public class UrlInfoController {

    @Autowired
    private UrlInfoService urlInfoService;
    @Autowired
    private UrlInfoChangeLogService urlInfoChangeLogService;

    @GetMapping(value = "/api/list")
    public ResultDTO<List<UrlInfo>> listApi(int environmentId){
        QueryWrapper<UrlInfo> urlInfoQueryWrapper = new QueryWrapper<>();
        urlInfoQueryWrapper.eq("environment_id",environmentId);
        return ResultDTO.success(urlInfoService.list(urlInfoQueryWrapper));
    }

    @Transactional(rollbackFor = Exception.class)
    @GetMapping(value = "/api/delete")
    public ResultDTO<Boolean> deleteApi(int id){
        //保存历史
        UrlInfo urlInfoDB = urlInfoService.getById(id);
        UrlInfoChangeLog urlInfoChangeLog = new UrlInfoChangeLog();
        BeanUtils.copyProperties(urlInfoDB,urlInfoChangeLog);
        urlInfoChangeLog.setOriginalId(id);
        urlInfoChangeLog.setCreateTime(new Date());
        urlInfoChangeLog.setId(null);
        urlInfoChangeLogService.save(urlInfoChangeLog);
        return ResultDTO.success(urlInfoService.removeById(id));
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/api/update")
    public ResultDTO<Boolean> updateApi(@RequestBody UrlInfoRequest request){
        UrlInfo urlInfo = new UrlInfo();
        BeanUtils.copyProperties(request,urlInfo);
        urlInfo.setUpdateTime(new Date());
        //保存历史
        UrlInfo urlInfoDB = urlInfoService.getById(request.getId());
        UrlInfoChangeLog urlInfoChangeLog = new UrlInfoChangeLog();
        BeanUtils.copyProperties(urlInfoDB,urlInfoChangeLog);
        urlInfoChangeLog.setOriginalId(request.getId());
        urlInfoChangeLog.setCreateTime(new Date());
        urlInfoChangeLog.setId(null);
        urlInfoChangeLogService.save(urlInfoChangeLog);
        return ResultDTO.success(urlInfoService.updateById(urlInfo));
    }

    @PostMapping(value = "/api/add")
    public ResultDTO<Boolean> addApi(@RequestBody UrlInfoRequest request){
        UrlInfo urlInfo = new UrlInfo();
        BeanUtils.copyProperties(request,urlInfo);
        urlInfo.setCreateTime(new Date());
        return ResultDTO.success( urlInfoService.save(urlInfo));
    }

    @PostMapping(value = "/api/useNumIncrease")
    public ResultDTO<Boolean> useNumIncrease(@RequestBody UrlInfoRequest request){
        return ResultDTO.success( urlInfoService.useNumIncrease(request.getId()));
    }
}
