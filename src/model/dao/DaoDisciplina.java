package model.dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NamedQuery;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import model.Disciplina;

@NamedQuery(name = "Disciplina.codigo", query = "SELECT d FROM Disciplina d WHERE d.codigo = :codigo")
public class DaoDisciplina {

	//abre uma conexao para o banco de dados.
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa_prjProva");
	private static EntityManager entityManager = entityManagerFactory.createEntityManager();

	public boolean salvar(Disciplina d) {
		return this.salvar(d, true);
	}

	public boolean salvar(Disciplina d, boolean autoCommit) {
		if (autoCommit == true)
			entityManager.getTransaction().begin(); //abre a tampa da caixa de disciplina
		try {
			entityManager.persist(d); // coloca disciplina la dentro
		} catch (PersistenceException e) {
			if (autoCommit == true)
				entityManager.getTransaction().rollback(); //se der erro, tira de dentro
			return false;
		}
		if (autoCommit == true)
			entityManager.getTransaction().commit(); //se der tudo certo, fecha a tampa
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

	public List<Disciplina> obterTodasDisciplinas() {
		Query query = entityManager.createQuery("SELECT d FROM Disciplina d");
		List<Disciplina> resultado  = query.getResultList();
		Disciplina[] retorno = new Disciplina[resultado.size()];
		for(int i = 0; i < resultado.size(); i++)
			retorno[i] = resultado.get(i);
		return List.of(retorno);

	}

	//ele está fazendo select d from disciplina d where d.codigo = "exemplo"
	public Disciplina obterDisciplinaPeloCodigo(String codigo) {
		Query query = entityManager.createNamedQuery("Disciplina.codigo"); // faz a busca citada na linha 15 select
		query.setParameter("codigo", codigo); //troca o parametro :codigo para codigo
		List<Disciplina> resultado  = query.getResultList(); //busca uma lista de disciplina
		if(resultado != null && resultado.size() > 0)
			return resultado.get(0); //pega o primeiro resultado da lista
		return null;
	}
}






