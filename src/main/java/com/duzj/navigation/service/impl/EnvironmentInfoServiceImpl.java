package com.duzj.navigation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duzj.navigation.entity.EnvironmentInfo;
import com.duzj.navigation.entity.UrlInfo;
import com.duzj.navigation.exceptions.SystemUserException;
import com.duzj.navigation.mapper.EnvironmentInfoMapper;
import com.duzj.navigation.service.EnvironmentInfoService;
import com.duzj.navigation.service.UrlInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
* @author duzengjie
* @description 针对表【environment_info(环境信息)】的数据库操作Service实现
* @createDate 2023-11-09 10:17:44
*/
@Service
public class EnvironmentInfoServiceImpl extends ServiceImpl<EnvironmentInfoMapper, EnvironmentInfo>
    implements EnvironmentInfoService{
@Autowired
    private UrlInfoService urlInfoService;
    @Override
    public boolean deleteEnvironment(int id) {
        QueryWrapper<UrlInfo> urlInfoQueryWrapper = new QueryWrapper<>();
        urlInfoQueryWrapper.eq("environment_id",id);
        List<UrlInfo> urlInfos = urlInfoService.list(urlInfoQueryWrapper);
        if(!ObjectUtils.isEmpty(urlInfos)){
            throw new SystemUserException("存在关联链接,请先清空链接在进行删除");
        }else {
            return removeById(id);
        }
    }
}




