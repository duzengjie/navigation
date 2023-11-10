package com.duzj.navigation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duzj.navigation.entity.EnvironmentInfo;
import com.duzj.navigation.entity.UrlInfo;
import com.duzj.navigation.entity.base.ResultDTO;
import com.duzj.navigation.entity.request.EnvironmentInfoRequest;
import com.duzj.navigation.entity.response.EnvironmentUrlListResponse;
import com.duzj.navigation.service.EnvironmentInfoService;
import com.duzj.navigation.service.UrlInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Date 2023/11/9 10:25
 * @Created by duzengjie
 */
@RestController
@RequestMapping("/env")
public class EnvironmentInfoController {

    @Autowired
    private EnvironmentInfoService environmentInfoService;
    @Autowired
    private UrlInfoService urlInfoService;

    @GetMapping(value = "/api/list")
    public ResultDTO<List<EnvironmentUrlListResponse>> listApi(){
        List<EnvironmentUrlListResponse> responses = new ArrayList<>();
        List<EnvironmentInfo> environmentInfoList = environmentInfoService.list();
        for (EnvironmentInfo environmentInfo : environmentInfoList) {
            EnvironmentUrlListResponse response = new EnvironmentUrlListResponse();
            QueryWrapper<UrlInfo> urlInfoQueryWrapper = new QueryWrapper<>();
            urlInfoQueryWrapper.eq("environment_id",environmentInfo.getId());
            response.setId(environmentInfo.getId());
            response.setName(environmentInfo.getName());
            response.setData(urlInfoService.list(urlInfoQueryWrapper));
            responses.add(response);
        }
        return ResultDTO.success(responses);
    }

    @GetMapping(value = "/api/delete")
    public ResultDTO<Boolean> deleteApi(int id){
        return ResultDTO.success(environmentInfoService.deleteEnvironment(id));
    }

    @PostMapping(value = "/api/update")
    public ResultDTO<Boolean> updateApi(@RequestBody EnvironmentInfoRequest request){
        EnvironmentInfo environmentInfo = new EnvironmentInfo();
        BeanUtils.copyProperties(request,environmentInfo);
        environmentInfo.setUpdateTime(new Date());
        return ResultDTO.success( environmentInfoService.updateById(environmentInfo));
    }

    @PostMapping(value = "/api/add")
    public ResultDTO<Boolean> addApi(@RequestBody EnvironmentInfoRequest request){
        EnvironmentInfo environmentInfo = new EnvironmentInfo();
        BeanUtils.copyProperties(request,environmentInfo);
        environmentInfo.setCreateTime(new Date());
        return ResultDTO.success( environmentInfoService.save(environmentInfo));
    }
}
