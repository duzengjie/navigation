package com.duzj.navigation.beanmapper;

import com.duzj.navigation.entity.UrlInfo;
import com.duzj.navigation.entity.UrlInfoExcelDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IUrlInfoMapper {
    IUrlInfoMapper INSTANCT = Mappers.getMapper(IUrlInfoMapper.class);

    UrlInfoExcelDTO urlInfo2urlInfoExcelDTO(UrlInfo urlInfo);

    List<UrlInfoExcelDTO> urlInfos2urlInfoExcelDTOs(List<UrlInfo> urlInfos);

}
