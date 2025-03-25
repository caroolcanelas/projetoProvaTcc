package model.dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NamedQuery;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import model.Disciplina;

@NamedQuery(name = "DisciplinaCodigo", query = "SELECT d FROM Disciplina d WHERE d.codigo = :codigo")
public class DaoDisciplina {

	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa_prjProva");
	private static EntityManager entityManager = entityManagerFactory.createEntityManager();

	public boolean salvar(Disciplina d) {
		return this.salvar(d, true);
	}

	public boolean salvar(Disciplina d, boolean autoCommit) {
		if (autoCommit == true)
			entityManager.getTransaction().begin();
		try {
			entityManager.persist(d);
		} catch (PersistenceException e) {
			if (autoCommit == true)
				entityManager.getTransaction().rollback();
			return false;
		}
		if (autoCommit == true)
			entityManager.getTransaction().commit();
		return true;
	}

	public boolean remover(Disciplina d) {
		return this.remover(d, true);
	}

	public boolean remover(Disciplina d, boolean autoCommit) {
		if (autoCommit == true)
			entityManager.getTransaction().begin();
		try {
			entityManager.remove(d);
		} catch (PersistenceException e) {
			if (autoCommit == true)
				entityManager.getTransaction().rollback();
			return false;
		}
		if (autoCommit == true)
			entityManager.getTransaction().commit();
		return true;
	}

	public Disciplina obterDisciplinaPeloCodigo(String codigo) {
		Query query = entityManager.createNamedQuery("Disciplina.codigo");
		query.setParameter("codigo", codigo);
		List<Disciplina> resultado  = query.getResultList();
		if(resultado != null && resultado.size() > 0)
			return resultado.get(0);
		return null;
	}
}
