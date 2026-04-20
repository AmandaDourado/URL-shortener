package com.example.demo.mapper;

import com.example.demo.dto.LinkPostDTO;
import com.example.demo.entities.Link;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LinkMapper {

    LinkMapper INSTANCE = Mappers.getMapper(LinkMapper.class);

    Link linkDTOLink(LinkPostDTO link);
}