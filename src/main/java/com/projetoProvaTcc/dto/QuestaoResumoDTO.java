package com.projetoProvaTcc.dto;

import com.projetoProvaTcc.entity.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestaoResumoDTO {
    private int id;
    private String comando;
    private NivelQuestao nivel;
    private TipoQuestao tipo;
}
