package com.example.myidejava.mapper;

import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.ContainerDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContainerMapper {

    ContainerMapper INSTANCE = Mappers.getMapper(ContainerMapper.class);

    Container toEntity(ContainerDto containerDto);

    List<ContainerDto> ofDtoList(List<Container> containers);

}
