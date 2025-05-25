package com.projetoProvaTcc.mapper;

import com.projetoProvaTcc.dto.TopicoDTO;
import com.projetoProvaTcc.entity.Topico;
import com.projetoProvaTcc.exception.ModelException;

public class TopicoMapper {

    public static TopicoDTO toDTO(Topico topico) {
        TopicoDTO dto = new TopicoDTO();
        dto.setId(topico.getId());
        dto.setNumOrdem(topico.getNumOrdem());
        dto.setNome(topico.getNome());
        dto.setConteudo(topico.getConteudo());

        return dto;

    }

    public static Topico toEntity(TopicoDTO dto) throws ModelException {
        Topico topico = new Topico();
        topico.setId(dto.getId());
        topico.setNumOrdem(dto.getNumOrdem());
        topico.setNome(dto.getNome());
        topico.setConteudo(dto.getConteudo());

        return topico;
    }

}
