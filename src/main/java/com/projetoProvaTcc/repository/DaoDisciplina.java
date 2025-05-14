package com.projetoProvaTcc.repository;

import com.projetoProvaTcc.entity.Disciplina;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class DaoDisciplina {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public boolean salvar(Disciplina d) {
		try {
			entityManager.persist(d);
			return true;
		} catch (PersistenceException e) {
			e.printStackTrace(); // para debugging
			return false;
		}
	}

	@Transactional
	public boolean remover(Disciplina d) {
		try {
			entityManager.remove(entityManager.contains(d) ? d : entityManager.merge(d));
			return true;
		} catch (PersistenceException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Disciplina> obterTodasDisciplinas() {
		return entityManager.createQuery("SELECT d FROM Disciplina d", Disciplina.class)
				.getResultList();
	}

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
