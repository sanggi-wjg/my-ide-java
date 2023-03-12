package com.example.myidejava.mapper;

import com.example.myidejava.domain.member.Member;
import com.example.myidejava.domain.member.SocialLogin;
import com.example.myidejava.dto.member.MemberResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mapping(source = "socialLogins", target = "socialLoginResponses")
    MemberResponse toRegisterResponse(Member member, List<SocialLogin> socialLogins);

}
