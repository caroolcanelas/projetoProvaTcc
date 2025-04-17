package controller;

import model.*;
import model.dao.*;

public class CtrlPrograma {

	public static void main(String[] args) {

		try {
			//Disciplina d = new Disciplina("POO","Programação Orientada a Objetos",4, "Aprender principíos de POO");
			//DaoDisciplina daoDisciplina = new DaoDisciplina();
			//daoDisciplina.salvar(d);

			//Topico t = new Topico(1, "Herança", "Explicação sobre herança", d);
			//DaoTopico dao = new DaoTopico();
			//dao.salvar(t);

			//Opcao o = new Opcao("A opção é a seguinte lalala", true);
			//DaoOpcao dao = new DaoOpcao();
			//dao.salvar(o);

			//Questao q = new Questao("Instrução inicial", "Suporte", "Comando",
				//	NivelQuestao.DIFICIL, TipoQuestao.RESPOSTA_UNICA, true);
			//DaoQuestao dao = new DaoQuestao();
			//dao.salvar(q); lalaa

			Tag t = new Tag("Nome da tag", "Assunto da tag");
			DaoTag dao = new DaoTag();
			dao.salvar(t);

		} catch (ModelException e) {
			e.printStackTrace();
		}
	}
}
