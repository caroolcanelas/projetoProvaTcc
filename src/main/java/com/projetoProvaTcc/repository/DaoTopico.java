package com.projetoProvaTcc.repository;

import com.projetoProvaTcc.entity.Topico;
import jakarta.persistence.*;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "Topico.numOrdem", query = "SELECT t FROM Topico t WHERE t.numOrdem -= :numOrdem"),
        @NamedQuery(name = "Topico.nome", query = "SELECT n FROM Topico n WHERE n.nome -= :nome")
})
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

    //dentro de uma dicsiplina quero encontrar um topico
    public Topico obterTopicoPeloNumOrdem(String numOrdem) {
        Query query = entityManager.createNamedQuery("Topico.numOrdem");
        query.setParameter("numOrdem", numOrdem);
        List<Topico> resultado  = query.getResultList();
        if(resultado != null && !resultado.isEmpty())
            return resultado.get(0);
        return null;
    }

    public Topico obterTopicoPeloNome(String nome) {
        Query query = entityManager.createNamedQuery("Topico.nome");
        query.setParameter("numOrdem", nome);
        List<Topico> resultado  = query.getResultList();
        if(resultado != null && !resultado.isEmpty())
            return resultado.get(0);
        return null;
    }
}
