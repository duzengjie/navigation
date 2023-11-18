package com.duzj.navigation.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duzj.navigation.beanmapper.IUrlInfoMapper;
import com.duzj.navigation.entity.EnvironmentInfo;
import com.duzj.navigation.entity.EnvironmentUrlListDTO;
import com.duzj.navigation.entity.UrlInfo;
import com.duzj.navigation.entity.UrlInfoExcelDTO;
import com.duzj.navigation.entity.response.EnvironmentUrlListResponse;
import com.duzj.navigation.exceptions.SystemUserException;
import com.duzj.navigation.mapper.EnvironmentInfoMapper;
import com.duzj.navigation.service.EnvironmentInfoService;
import com.duzj.navigation.service.UrlInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author duzengjie
 * @description 针对表【environment_info(环境信息)】的数据库操作Service实现
 * @createDate 2023-11-09 10:17:44
 */
@Service
public class EnvironmentInfoServiceImpl extends ServiceImpl<EnvironmentInfoMapper, EnvironmentInfo>
        implements EnvironmentInfoService {
    @Autowired
    private UrlInfoService urlInfoService;

    @Override
    public boolean deleteEnvironment(int id) {
        QueryWrapper<UrlInfo> urlInfoQueryWrapper = new QueryWrapper<>();
        urlInfoQueryWrapper.eq("environment_id", id);
        List<UrlInfo> urlInfos = urlInfoService.list(urlInfoQueryWrapper);
        if (!ObjectUtils.isEmpty(urlInfos)) {
            throw new SystemUserException("存在关联链接,请先清空链接在进行删除");
        } else {
            return removeById(id);
        }
    }

    @Override
    public List<EnvironmentUrlListDTO> selectAll() {
        List<EnvironmentUrlListDTO> environmentUrlListDTOS = new ArrayList<>();
        List<EnvironmentInfo> environmentInfoList = list();
        for (EnvironmentInfo environmentInfo : environmentInfoList) {
            EnvironmentUrlListDTO environmentUrlListDTO = new EnvironmentUrlListDTO();
            QueryWrapper<UrlInfo> urlInfoQueryWrapper = new QueryWrapper<>();
            urlInfoQueryWrapper.eq("environment_id", environmentInfo.getId());
            environmentUrlListDTO.setId(environmentInfo.getId());
            environmentUrlListDTO.setName(environmentInfo.getName());
            environmentUrlListDTO.setData(urlInfoService.list(urlInfoQueryWrapper));
            environmentUrlListDTOS.add(environmentUrlListDTO);
        }
        return environmentUrlListDTOS;
    }

    @Override
    public void downloadAllByExcel(HttpServletResponse response) {
        String fileName = DateUtils.format(new Date(),"yyyy-MM-dd") + ".xlsx";
        List<EnvironmentUrlListDTO> environmentUrlListDTOS = selectAll();

        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), UrlInfoExcelDTO.class).build()) {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/octet-stream");

            for (int i = 0; i < environmentUrlListDTOS.size(); i++) {
                // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样
                WriteSheet writeSheet = EasyExcel.writerSheet(i, environmentUrlListDTOS.get(i).getName()).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<UrlInfo> data = environmentUrlListDTOS.get(i).getData();
                excelWriter.write(IUrlInfoMapper.INSTANCT.urlInfos2urlInfoExcelDTOs(data), writeSheet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}




