package com.duzj.navigation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duzj.navigation.entity.UrlInfo;
import com.duzj.navigation.entity.base.ResultDTO;
import com.duzj.navigation.entity.request.UrlInfoRequest;
import com.duzj.navigation.service.UrlInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(value = "/api/list")
    public ResultDTO<List<UrlInfo>> listApi(int environmentId){
        QueryWrapper<UrlInfo> urlInfoQueryWrapper = new QueryWrapper<>();
        urlInfoQueryWrapper.eq("environment_id",environmentId);
        return ResultDTO.success(urlInfoService.list(urlInfoQueryWrapper));
    }

    @GetMapping(value = "/api/delete")
    public ResultDTO<Boolean> deleteApi(int id){
        return ResultDTO.success(urlInfoService.removeById(id));
    }

    @PostMapping(value = "/api/update")
    public ResultDTO<Boolean> updateApi(@RequestBody UrlInfoRequest request){
        UrlInfo urlInfo = new UrlInfo();
        BeanUtils.copyProperties(request,urlInfo);
        urlInfo.setUpdateTime(new Date());
        return ResultDTO.success( urlInfoService.updateById(urlInfo));
    }

    @PostMapping(value = "/api/add")
    public ResultDTO<Boolean> addApi(@RequestBody UrlInfoRequest request){
        UrlInfo urlInfo = new UrlInfo();
        BeanUtils.copyProperties(request,urlInfo);
        urlInfo.setCreateTime(new Date());
        return ResultDTO.success( urlInfoService.save(urlInfo));
    }
}
