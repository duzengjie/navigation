package com.duzj.navigation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.duzj.navigation.entity.EnvironmentInfo;
import com.duzj.navigation.entity.EnvironmentUrlListDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
* @author duzengjie
* @description 针对表【environment_info(环境信息)】的数据库操作Service
* @createDate 2023-11-09 10:17:44
*/
public interface EnvironmentInfoService extends IService<EnvironmentInfo> {
    /**
     * 删除前需要校验是否有链接存在,如果有无法删除
     * @param id 环境主键
     */
    public boolean deleteEnvironment(int id);

    public List<EnvironmentUrlListDTO> selectAll();

    public void downloadAllByExcel(HttpServletResponse response);
}
