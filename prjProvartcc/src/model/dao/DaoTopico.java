package model.dao;

import model.Disciplina;
import model.Topico;

import javax.persistence.*;
import java.util.List;

@NamedQuery(name = "TopicoNumOrdem", query = "SELECT t FROM Topico t WHERE t.numOrdem -= :numOrdem")
public class DaoTopico {


    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa_prjProva");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();

    public boolean salvar(Topico t) {
        return this.salvar(t, true);
    }

    public boolean salvar(Topico t, boolean autoCommit) {
        if (autoCommit == true)
            entityManager.getTransaction().begin();
        try {
            entityManager.persist(t);
        } catch (PersistenceException e) {
            if (autoCommit == true)
                entityManager.getTransaction().rollback();
            return false;
        }
        if (autoCommit == true)
            entityManager.getTransaction().commit();
        return true;
    }

    public boolean remover(Topico t) {
        return this.remover(t, true);
    }

    public boolean remover(Topico t, boolean autoCommit) {
        if (autoCommit == true)
            entityManager.getTransaction().begin();
        try {
            entityManager.remove(t);
        } catch (PersistenceException e) {
            if (autoCommit == true)
                entityManager.getTransaction().rollback();
            return false;
        }
        if (autoCommit == true)
            entityManager.getTransaction().commit();
        return true;
    }


    //Necessario obter todos os topicos cadastrados ?

    //dentro de uma dicsiplina quero encontrar um topico
    public Topico obterTopicoPeloNumOrdem(String numOrdem) {
        Query query = entityManager.createNamedQuery("Topico.numOrdem");
        query.setParameter("numOrdem", numOrdem);
        List<Topico> resultado  = query.getResultList();
        if(resultado != null && resultado.size() > 0)
            return resultado.get(0);
        return null;
    }

    // quero todos os topicos de uma disciplina
//    public List<Topico> obterTopicosPorDisciplina(Disciplina disciplina) {
//        Query query = entityManager.createQuery(
//                //"SELECT t FROM Topico t WHERE
//        );
//        query.setParameter("disciplina", disciplina);
//        return query.getResultList();
//    }
























}
