package com.projetoProvaTcc.mapper;

import com.projetoProvaTcc.dto.RecursoDTO;
import com.projetoProvaTcc.entity.Recurso;
import com.projetoProvaTcc.exception.ModelException;
import org.springframework.stereotype.Component;

@Component
public class RecursoMapper {

    public RecursoDTO toDTO(Recurso recurso) {
        if (recurso == null) return null;
        return new RecursoDTO(
                recurso.getId(),
                recurso.getConteudo()
        );
    }

    public Recurso toEntity(RecursoDTO dto) throws ModelException {
        if (dto == null) return null;
        Recurso recurso = new Recurso();
        recurso.setId(dto.getId());
        recurso.setConteudo(dto.getConteudo());
        return recurso;
    }
}
