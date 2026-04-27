package com.duzj.navigation.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duzj.navigation.beanmapper.IUrlInfoMapper;
import com.duzj.navigation.entity.EnvironmentInfo;
import com.duzj.navigation.entity.UrlInfo;
import com.duzj.navigation.entity.dto.EnvironmentUrlListDTO;
import com.duzj.navigation.entity.dto.UrlInfoExcelDTO;
import com.duzj.navigation.excel.UrlInfoDataListener;
import com.duzj.navigation.exceptions.SystemUserException;
import com.duzj.navigation.mapper.EnvironmentInfoMapper;
import com.duzj.navigation.mapper.UrlInfoMapper;
import com.duzj.navigation.service.EnvironmentInfoService;
import com.duzj.navigation.service.UrlInfoService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.fesod.sheet.ExcelReader;
import org.apache.fesod.sheet.ExcelWriter;
import org.apache.fesod.sheet.FesodSheet;
import org.apache.fesod.sheet.read.metadata.ReadSheet;
import org.apache.fesod.sheet.write.metadata.WriteSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
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
            urlInfoQueryWrapper.orderByAsc("order_num").orderByDesc("use_num");
            environmentUrlListDTO.setId(environmentInfo.getId());
            environmentUrlListDTO.setName(environmentInfo.getName());
            environmentUrlListDTO.setData(urlInfoService.list(urlInfoQueryWrapper));
            environmentUrlListDTOS.add(environmentUrlListDTO);
        }
        return environmentUrlListDTOS;
    }

    @Override
    public void downloadAllByExcel(HttpServletResponse response) {
        String fileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".xlsx";
        List<EnvironmentUrlListDTO> environmentUrlListDTOS = selectAll();

        try {
            response.setCharacterEncoding("UTF-8");
            response.addHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/octet-stream");

            try (ExcelWriter fesodWriter = FesodSheet.write(response.getOutputStream(), UrlInfoExcelDTO.class).build()) {
                for (int i = 0; i < environmentUrlListDTOS.size(); i++) {
                    String sheetName = environmentUrlListDTOS.get(i).getId() + "-" + environmentUrlListDTOS.get(i).getName();
                    WriteSheet fesodWriteSheet = FesodSheet.writerSheet(i, sheetName).build();
                    List<UrlInfo> data = environmentUrlListDTOS.get(i).getData();
                    fesodWriter.write(IUrlInfoMapper.INSTANCT.urlInfos2urlInfoExcelDTOs(data), fesodWriteSheet);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void backupRecoverByExcel(MultipartFile file) {
        if (file.isEmpty()) {
            throw new SystemUserException("文件上传异常,文件为空");
        }
        UrlInfoDataListener urlInfoDataListener = new UrlInfoDataListener(urlInfoService);
        environmentInfoMapper.delete(new QueryWrapper<>());
        urlInfoMapper.delete(new QueryWrapper<>());
        try (InputStream inputStream = file.getInputStream()) {
            ExcelReader fesodReader = FesodSheet.read(inputStream, UrlInfoExcelDTO.class, urlInfoDataListener).build();
            List<ReadSheet> sheets = fesodReader.excelExecutor().sheetList();
            for (ReadSheet sheet : sheets) {
                String[] split = sheet.getSheetName().split("-");
                EnvironmentInfo environmentInfo = new EnvironmentInfo();
                environmentInfo.setId(Integer.parseInt(split[0]));
                environmentInfo.setName(split[1]);
                environmentInfo.setCreateTime(new Date());
                if (environmentInfoMapper.insert(environmentInfo) == 1) {
                    fesodReader.read(sheet);
                } else {
                    throw new SystemUserException("恢复环境异常:" + sheet.getSheetName());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
