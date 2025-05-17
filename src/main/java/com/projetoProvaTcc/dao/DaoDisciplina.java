package com.projetoProvaTcc.dao;

import com.projetoProvaTcc.entity.Disciplina;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DaoDisciplina {

	@PersistenceContext
	private EntityManager entityManager;

	//metodos salvar, remover e obterTodarDisciplinas excluidos pois utilizei metodos prontos do JPARepository na classe DisciplinaService
	//Como esses metodos tem interação com banco, podem ser movidos para DisciplinaRepository e assim armazenar todas queries em uma classe JPArepository

	public Disciplina obterDisciplinaPeloCodigo(String codigo) {
		List<Disciplina> resultado = entityManager
				.createQuery("SELECT d FROM Disciplina d WHERE d.codigo = :codigo", Disciplina.class)
				.setParameter("codigo", codigo)
				.getResultList();

		return resultado.isEmpty() ? null : resultado.get(0);
	}

	public Disciplina obterDisciplinaPeloNome(String nome) {
		List<Disciplina> resultado = entityManager
				.createQuery("SELECT d FROM Disciplina d WHERE d.nome = :nome", Disciplina.class)
				.setParameter("nome", nome)
				.getResultList();

		return resultado.isEmpty() ? null : resultado.get(0);
	}
}
