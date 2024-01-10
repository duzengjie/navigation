package com.duzj.navigation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duzj.navigation.entity.UrlInfo;
import com.duzj.navigation.service.UrlInfoService;
import com.duzj.navigation.mapper.UrlInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author duzengjie
* @description 针对表【url_info(链接信息)】的数据库操作Service实现
* @createDate 2024-01-10 11:11:46
*/
@Service
public class UrlInfoServiceImpl extends ServiceImpl<UrlInfoMapper, UrlInfo> implements UrlInfoService{
    @Autowired
    private UrlInfoMapper urlInfoMapper;

    @Override
    public boolean useNumIncrease(int id) {
        return urlInfoMapper.useNumIncrease(id);
    }
}




