package controller;

import model.Disciplina;
import model.ModelException;
import model.dao.DaoDisciplina;

public class CtrlPrograma {

	public static void main(String[] args) {

		try {
			Disciplina d = new Disciplina("POO","Programação Orientada a Objetos",4);
			
			DaoDisciplina dao = new DaoDisciplina();
			dao.salvar(d);
		
		} catch (ModelException e) {
			e.printStackTrace();
		}
	}
}
