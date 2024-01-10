package com.duzj.navigation.service;

import com.duzj.navigation.entity.UrlInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author duzengjie
* @description 针对表【url_info(链接信息)】的数据库操作Service
* @createDate 2024-01-10 11:11:46
*/
public interface UrlInfoService extends IService<UrlInfo> {
    /**
     * 使用次数+1
     * @param id
     * @return
     */
    boolean useNumIncrease(int id);
}
