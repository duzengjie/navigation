package com.duzj.navigation.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duzj.navigation.beanmapper.IUrlInfoMapper;
import com.duzj.navigation.entity.EnvironmentInfo;
import com.duzj.navigation.entity.EnvironmentUrlListDTO;
import com.duzj.navigation.entity.UrlInfo;
import com.duzj.navigation.entity.UrlInfoExcelDTO;
import com.duzj.navigation.excel.UrlInfoDataListener;
import com.duzj.navigation.exceptions.SystemUserException;
import com.duzj.navigation.mapper.EnvironmentInfoMapper;
import com.duzj.navigation.mapper.UrlInfoMapper;
import com.duzj.navigation.service.EnvironmentInfoService;
import com.duzj.navigation.service.UrlInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
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
    @Autowired
    private UrlInfoMapper urlInfoMapper;
    @Autowired
    private EnvironmentInfoMapper environmentInfoMapper;

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
        String fileName = DateUtils.format(new Date(), "yyyy-MM-dd") + ".xlsx";
        List<EnvironmentUrlListDTO> environmentUrlListDTOS = selectAll();

        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), UrlInfoExcelDTO.class).build()) {
            response.setCharacterEncoding("UTF-8");
            response.addHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/octet-stream");

            for (int i = 0; i < environmentUrlListDTOS.size(); i++) {
                String sheetName = environmentUrlListDTOS.get(i).getId() + "-" + environmentUrlListDTOS.get(i).getName();
                // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样
                WriteSheet writeSheet = EasyExcel.writerSheet(i, sheetName).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<UrlInfo> data = environmentUrlListDTOS.get(i).getData();
                excelWriter.write(IUrlInfoMapper.INSTANCT.urlInfos2urlInfoExcelDTOs(data), writeSheet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void backupRecoverByExcel(MultipartFile file) {
        if (file.isEmpty()){
            throw new SystemUserException("文件上传异常,文件为空");
        }
        UrlInfoDataListener urlInfoDataListener = new UrlInfoDataListener(urlInfoService);
        //清空数据
        environmentInfoMapper.truncateTable();
        urlInfoMapper.truncateTable();
        try (InputStream inputStream = file.getInputStream()) {
            ExcelReader excelReader = EasyExcel.read(inputStream, UrlInfo.class, urlInfoDataListener).build();
            List<ReadSheet> sheets = excelReader.excelExecutor().sheetList();
            for (ReadSheet sheet : sheets) {
                String[] split = sheet.getSheetName().split("-");
                EnvironmentInfo environmentInfo = new EnvironmentInfo();
                environmentInfo.setId(Integer.parseInt(split[0]));
                environmentInfo.setName(split[1]);
                environmentInfo.setCreateTime(new Date());
                if (environmentInfoMapper.insert(environmentInfo) == 1) {
                    excelReader.read(sheet);
                } else {
                    throw new SystemUserException("恢复环境异常:" + sheet.getSheetName());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}




