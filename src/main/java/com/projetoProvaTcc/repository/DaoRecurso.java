package com.projetoProvaTcc.repository;

import com.projetoProvaTcc.model.Recurso;

import jakarta.persistence.*;
import java.util.List;

@NamedQuery(name = "Recurso.codigo", query = "SELECT r FROM Recurso r WHERE r.codigo = :codigo")
public class DaoRecurso {

    //abre uma conexao para o banco de dados.
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa_prjProva");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();

    public boolean salvar(Recurso recurso) {
        return this.salvar(recurso, true);
    }

    public boolean salvar(Recurso recurso, boolean autoCommit) {
        if (autoCommit == true)
            entityManager.getTransaction().begin();
        try {
            entityManager.persist(recurso);
        } catch (PersistenceException e) {
            if (autoCommit == true)
                entityManager.getTransaction().rollback();
            return false;
        }
        if (autoCommit == true)
            entityManager.getTransaction().commit();
        return true;
    }

    public boolean remover(Recurso recurso) {
        return this.remover(recurso, true);
    }

    public boolean remover(Recurso recurso, boolean autoCommit) {
        if (autoCommit == true)
            entityManager.getTransaction().begin();
        try {
            entityManager.remove(recurso);
        } catch (PersistenceException e) {
            if (autoCommit == true)
                entityManager.getTransaction().rollback();
            return false;
        }
        if (autoCommit == true)
            entityManager.getTransaction().commit();
        return true;
    }

    //precisa???????
    public List<Recurso> obterTodosRecursos() {
        Query query = entityManager.createQuery("SELECT r FROM Recurso r");
        List<Recurso> resultado  = query.getResultList();
        Recurso[] retorno = new Recurso[resultado.size()];
        for(int i = 0; i < resultado.size(); i++)
            retorno[i] = resultado.get(i);
        return List.of(retorno);

    }
}
