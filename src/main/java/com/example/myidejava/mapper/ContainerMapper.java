package com.example.myidejava.mapper;

import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.ContainerDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ContainerMapper {

    ContainerMapper INSTANCE = Mappers.getMapper(ContainerMapper.class);

    Container toEntity(ContainerDto containerDto);

}
