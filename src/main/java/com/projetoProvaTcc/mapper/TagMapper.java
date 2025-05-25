package com.projetoProvaTcc.mapper;

import com.projetoProvaTcc.dto.TagDTO;
import com.projetoProvaTcc.entity.Tag;
import com.projetoProvaTcc.exception.ModelException;

public class TagMapper {


    public static TagDTO toDTO(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setAssunto(tag.getAssunto());
        dto.setTagName(tag.getTagName());
        return dto;
    }

    public static Tag toEntity(TagDTO dto) throws ModelException {
        Tag tag = new Tag();
        tag.setId(dto.getId());
        tag.setAssunto(dto.getAssunto());
        tag.setTagName(dto.getTagName());
        return tag;
    }
}
