package com.example.myidejava.mapper;

import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.ContainerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContainerMapper {

    ContainerMapper INSTANCE = Mappers.getMapper(ContainerMapper.class);

    @Mapping(target = "codeExecutorType", ignore = true)
    Container toEntity(ContainerResponse containerResponse);

    List<ContainerResponse> ofDtoList(List<Container> containers);

}
